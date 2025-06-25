package com.tsix_hack.spam_ai_detection.service;

import com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager.MessageRequest;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Service
public class MailParser {
    private final Session session;

    public MailParser() {
        session = Session.getDefaultInstance(new Properties(), null);
    }

    public void fixPermissions() throws IOException, InterruptedException {
        Process p = new ProcessBuilder("sudo", "/usr/local/bin/fix_mail_permissions.sh").start();
        int code = p.waitFor();
        if (code != 0) throw new RuntimeException("Erreur script, code " + code);
    }


    public List<MessageRequest> readAllMessages(String directoryPath) throws Exception {
        fixPermissions() ;
        List<MessageRequest> messages = new ArrayList<>();
        Path dir = Paths.get(directoryPath);
        Path curDir = dir.getParent().resolve("cur");

        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Not a valid directory: " + directoryPath);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    MessageRequest message = parseMail(file);
                    if (message != null) {
                        messages.add(message);
                        moveToCur(file, curDir);
                    }
                }
            }
        }

        return messages;
    }

    private void moveToCur(Path file, Path curDir) {
        try {
            String newFileName = file.getFileName().toString() + ":2,";
            Path destination = curDir.resolve(newFileName);
            Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Déplacé vers cur/: " + destination.getFileName());
        } catch (Exception e) {
            System.err.println("❌ Erreur de déplacement vers cur/: " + file.getFileName());
            e.printStackTrace();
        }
    }


    private MessageRequest parseMail(Path filePath) {
        try (InputStream is = Files.newInputStream(filePath)) {
            MimeMessage mimeMessage = new MimeMessage(session, is);

            // Récupération propre de l'expéditeur
            String senderRaw = mimeMessage.getFrom() != null ? mimeMessage.getFrom()[0].toString() : null;
            String sender = extractEmail(senderRaw);

            // Récupération propre des destinataires
            List<String> receivers = new ArrayList<>();
            Address[] to = mimeMessage.getRecipients(Message.RecipientType.TO);
            if (to != null) {
                for (Address addr : to) {
                    receivers.add(extractEmail(addr.toString()));
                }
            }

            String subject = mimeMessage.getSubject();
            String body = extractContent(mimeMessage);

            return new MessageRequest(sender, subject, body, receivers);

        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du mail: " + filePath);
            e.printStackTrace();
            return null;
        }
    }


    private String extractEmail(String rawAddress) {
        if (rawAddress == null) return null;
        rawAddress = rawAddress.trim();

        if (rawAddress.contains("<") && rawAddress.contains(">")) {
            int start = rawAddress.indexOf('<') + 1;
            int end = rawAddress.indexOf('>');
            return rawAddress.substring(start, end).trim();
        }

        return rawAddress.replace("\"", "").trim();
    }


    private String extractContent(Part part) throws Exception {
        Object content = part.getContent();

        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof Multipart) {
            return extractHtmlFromMultipart((Multipart) content);
        }
        return "";
    }

    private String extractHtmlFromMultipart(Multipart multipart) throws Exception {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);

            if (part.isMimeType("text/html")) {
                return part.getContent().toString();
            } else if (part.getContent() instanceof Multipart) {
                String html = extractHtmlFromMultipart((Multipart) part.getContent());
                if (!html.isEmpty()) {
                    return html.replace("\n" , "");
                }
            }
        }


        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.isMimeType("text/plain")) {
                return part.getContent().toString();
            }
        }

        return "";
    }

}
