package com.example.javamailer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class AmazonSESService {

    @Value("${aws.ses.access-key}")
    private String accessKey;

    @Value("${aws.ses.secret-key}")
    private String secretKey;

    @Value("${aws.ses.region}")
    private String region;

    @Value("${aws.ses.from.email}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            // Create AWS credentials
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
            
            // Create SES client
            SesClient sesClient = SesClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();

            // Create the email destination
            Destination destination = Destination.builder()
                    .toAddresses(to)
                    .build();

            // Create the message
            Message message = Message.builder()
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
                    .build();

            // Create the send email request
            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .source(fromEmail)
                    .destination(destination)
                    .message(message)
                    .build();

            // Send the email
            SendEmailResponse result = sesClient.sendEmail(sendEmailRequest);
            
            // Log the message ID for tracking
            System.out.println("Email sent successfully. Message ID: " + result.messageId());

        } catch (SesException e) {
            System.err.println("Error sending email: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to send email via Amazon SES: " + e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Failed to send email via Amazon SES: " + e.getMessage(), e);
        }
    }
}
