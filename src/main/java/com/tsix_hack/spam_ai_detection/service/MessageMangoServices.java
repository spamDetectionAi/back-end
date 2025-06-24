package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.configuration.WebSocketSessionListener;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.*;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MongoMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.util.*;

@Service
@AllArgsConstructor
public class MessageMangoServices {
    private final Detection detection ;
    private final MongoMessageRepository mongoMessageRepository ;
    private final AccountRepository accountRepository ;
    private final WebSocketSessionListener socketSessionListener ;
    private final MailParser mailParser ;


    public Map<String, Object> senderOrganisation(String sender){
        Map<String , Object> result = new HashMap<>() ;
        Optional<Account> account = accountRepository.findAccountByEmail(sender) ;
        if (account.isPresent()){
            result.put("email" , sender) ;
            result.put("id" , account.get().getId()) ;
            result.put("senderType" , EmailType.LOCAL) ;
            return result ;
        } else {
            result.put("email" , sender) ;
            result.put("senderType" , EmailType.FOREIGN) ;
            return result ;
        }
    }

    record IdEmail(UUID id , String email){}

    public Map<String , Object> receiversOrganisation(List<String> emails) {
        Map<String , Object> organised = new HashMap<>() ;
        List<IdEmail> local = new ArrayList<>() ;
        List<String> foreign = new ArrayList<>() ;
        List<String> notFound = new ArrayList<>() ;
        List<Receiver> receivers = new ArrayList<>() ;
        for (String email : emails) {
            Optional<Account> account = accountRepository.findAccountByEmail(email) ;
            if (account.isPresent()){
                IdEmail idEmail = new IdEmail(account.get().getId() , email) ;
                local.add(idEmail) ;
                Receiver receiver = new Receiver(idEmail.email() , EmailType.LOCAL , Status.SENT) ;
                receivers.add(receiver) ;
            } else if (!account.isPresent() && email.contains("@maily.tech")) {
                notFound.add(email) ;
                Receiver receiver = new Receiver(email , EmailType.NOT_FOUND , Status.UNSENT) ;
                receivers.add(receiver) ;
            } else {
                foreign.add(email) ;
                receivers.add(new Receiver(email , EmailType.FOREIGN , Status.SENT)) ;
            }
        }
        organised.put("local" , local) ;
        organised.put("foreign" , foreign) ;
        organised.put("notFound" , notFound) ;
        organised.put("receivers" , receivers) ;
        return organised ;
    }

    public String removeHtmlTags(String body) {
        return body.replaceAll("<[^>]*>", "");
    }

    public MessagesMongoDb sendMessage(MessageRequest messageRequest) {

        Map<String , Object> organisedSender = senderOrganisation(messageRequest.sender()) ;
        String email = organisedSender.get("email").toString();
        EmailType senderType = (EmailType) organisedSender.get("senderType");
        UUID id = senderType.equals(EmailType.LOCAL) ? (UUID) organisedSender.get("id") : null ;
        MessageSender messageSender = new MessageSender(email , senderType);

        Map<String , Object> organisedReceivers = receiversOrganisation(messageRequest.receivers());
        List<Receiver> receivers = (List<Receiver>) organisedReceivers.get("receivers");

        boolean isSpam = detection.callDetector(removeHtmlTags(messageRequest.body()));
        System.out.println(isSpam);

        MessagesMongoDb msgToSave = new MessagesMongoDb(messageRequest.object() , messageRequest.body() , messageSender ,receivers , isSpam );
        List<IdEmail> local = (organisedReceivers.get("local") != null) ? (List<IdEmail>) organisedReceivers.get("local") : null ;
        List<String> foreign = (organisedReceivers.get("foreign") != null) ? (List<String>) organisedReceivers.get("foreign") : null ;
        List<String> notFound = (organisedReceivers.get("notFound") != null) ? (List<String>) organisedReceivers.get("notFound") : null;
        MessagesMongoDb saved = mongoMessageRepository.save(msgToSave);
        if(local != null){
            List<UUID> uuids = local.stream()
                    .map(IdEmail::id)
                    .toList() ;

            if (!isSpam){
                socketSessionListener.sendMessageToLocal(uuids , saved);
            }else {
                socketSessionListener.sendMessageToSpam(uuids , saved);
            }
        } else if (foreign != null) {
            sendToForeign(messageRequest);
        }

        if (id != null) {
            socketSessionListener.sendToLocalSender(id , saved);
        }
        return saved ;
    }
    public void sendToForeign(MessageRequest message) {
        System.out.println("sending to foreign" + message.sender());
    }

    public List<MessagesMongoDb> downloadFromServer(String email) throws Exception {
        String name = email.substring(0 , email.indexOf("@")) ;
      List<MessageRequest> request =  mailParser.readAllMessages("/var/mail/vmail/maily.tech/"+name+"new") ;
      List<MessagesMongoDb> messages = new ArrayList<>() ;
      for(MessageRequest m : request){
         MessagesMongoDb msg =  sendMessage(m) ;
         messages.add(msg) ;
      }
      return messages ;
    }
}
