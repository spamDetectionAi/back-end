package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.EntityMessages.*;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.ForeignSender;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.LocalSender;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.MessageRequest;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.MessageToSend;
import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.Sender;
import com.tsix_hack.spam_ai_detection.entities.account.accountForm.Account;
import com.tsix_hack.spam_ai_detection.repositories.AccountRepository;
import com.tsix_hack.spam_ai_detection.repositories.MessagesRepositories.MongoMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class MessageMangoServices {
    private final Detection detection ;
    private final MongoMessageRepository mongoMessageRepository ;
    private final AccountRepository accountRepository ;

    public Map<String, Object> senderOrganisation(Sender sender){
        Map<String , Object> result = new HashMap<>() ;
        if (sender instanceof LocalSender localSender){
                Account account = accountRepository.findAccountById(localSender.senderId()) ;
            result.put("email" , account.getEmail()) ;
            result.put("id" , account.getId()) ;
            result.put("senderType" , EmailType.LOCAL) ;
            return result ;
        } else if (sender instanceof ForeignSender foreignSender) {
            result.put("email" , foreignSender.email()) ;
            result.put("senderType" , EmailType.FOREIGN) ;
            return result ;
        }
        return null;
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

    public MessagesMongoDb sendMessage(MessageRequest messageRequest) {

        Map<String , Object> organisedSender = senderOrganisation(messageRequest.sender()) ;
        String email = organisedSender.get("email").toString();
        EmailType senderType = (EmailType) organisedSender.get("senderType");
        UUID id = senderType.equals(EmailType.LOCAL) ? (UUID) organisedSender.get("id") : null ;
        MessageSender messageSender = new MessageSender(email , senderType);
        Map<String , Object> organisedReceivers = receiversOrganisation(messageRequest.receivers());
        List<Receiver> receivers = (List<Receiver>) organisedReceivers.get("receivers");

        MessagesMongoDb msgToSave = new MessagesMongoDb(messageRequest.object() , messageRequest.body() , messageSender ,receivers );
        return mongoMessageRepository.save(msgToSave);
        /*List<IdEmail> local = (organisedReceivers.get("local") != null) ? (List<IdEmail>) organisedReceivers.get("local") : new ArrayList<>() ;
        List<String> foreign = (organisedReceivers.get("foreign") != null) ? (List<String>) organisedReceivers.get("foreign") : new ArrayList<>() ;
        List<String> notFound = (organisedReceivers.get("notFound") != null) ? (List<String>) organisedReceivers.get("notFound") : new ArrayList<>() ;
        MessageToSend msgToSend = new MessageToSend(email , messageRequest.object() , messageRequest.object() , Instant.now()) ;*/

    }
    
    public void sendToForeign(MessageRequest message) {
        System.out.println("sending to foreign" + message.sender());
    }


}
