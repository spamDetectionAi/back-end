package com.tsix_hack.spam_ai_detection.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
@Service
public class SESmailSender {

    private final String access_key_id = System.getenv("AWS_KEY") ;
    private final String access_key_secret = System.getenv("AWS_SECRET") ;
    private Region region = Region.EU_WEST_3 ;
    private SesClient sesClient ;

    public SESmailSender (){
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(access_key_id , access_key_secret);
        this.sesClient = SesClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public void sendEmail(String from , String to , String subject , String body ){
        try {
            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder()
                            .toAddresses(to)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder()
                                    .data(subject)
                                    .charset("UTF-8")
                                    .build())
                            .body(Body.builder()
                                    .text(Content.builder()
                                            .data(body)
                                            .charset("UTF-8")
                                            .build())
                                    .build())
                            .build())
                    .source(from)
                    .build();

            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            System.out.println("Email envoyé. Message ID: " + response.messageId());

        } catch (SesException e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.awsErrorDetails().errorMessage());
        }
    }


}
