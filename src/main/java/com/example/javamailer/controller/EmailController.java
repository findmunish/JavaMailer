package com.example.javamailer.controller;

import com.example.javamailer.service.EmailService;
import com.example.javamailer.service.SendGridService;
import com.example.javamailer.service.AmazonSESService;
import com.example.javamailer.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
@Tag(name = "Email Service", description = "API for sending emails via multiple providers")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SendGridService sendGridService;

    @Autowired
    private AmazonSESService amazonSESService;

    @GetMapping("/")
    @Operation(summary = "Welcome endpoint", description = "Get welcome message from the application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Welcome message retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Welcome to Java Mailer Application!\", \"status\": \"success\"}")))
    })
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Java Mailer Application!");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-email-smtp")
    @Operation(summary = "Send email via SMTP", description = "Send email using Gmail SMTP (Java Mailer)")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Email sent successfully via SMTP (Java Mailer)!\", \"status\": \"success\", \"method\": \"SMTP\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request - validation failed or email sending failed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Failed to send email via SMTP\", \"status\": \"error\", \"method\": \"SMTP\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required")
    })
    public ResponseEntity<Map<String, String>> sendEmailSmtp(
            @Parameter(description = "Email request details", required = true)
            @Valid @RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email sent successfully via SMTP (Java Mailer)!");
            response.put("status", "success");
            response.put("method", "SMTP");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to send email via SMTP");
            response.put("status", "error");
            response.put("method", "SMTP");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/send-email-sendgrid")
    @Operation(summary = "Send email via SendGrid", description = "Send email using SendGrid API")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Email sent successfully via SendGrid!\", \"status\": \"success\", \"method\": \"SendGrid\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request - validation failed or email sending failed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Failed to send email via SendGrid\", \"status\": \"error\", \"method\": \"SendGrid\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required")
    })
    public ResponseEntity<Map<String, String>> sendEmailSendGrid(
            @Parameter(description = "Email request details", required = true)
            @Valid @RequestBody EmailRequest emailRequest) {
        try {
            sendGridService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email sent successfully via SendGrid!");
            response.put("status", "success");
            response.put("method", "SendGrid");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to send email via SendGrid");
            response.put("status", "error");
            response.put("method", "SendGrid");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/send-email-ses")
    @Operation(summary = "Send email via Amazon SES", description = "Send email using Amazon Simple Email Service")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Email sent successfully via Amazon SES!\", \"status\": \"success\", \"method\": \"Amazon SES\"}"))),
            @ApiResponse(responseCode = "400", description = "Bad request - validation failed or email sending failed",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Failed to send email via Amazon SES\", \"status\": \"error\", \"method\": \"Amazon SES\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required")
    })
    public ResponseEntity<Map<String, String>> sendEmailSES(
            @Parameter(description = "Email request details", required = true)
            @Valid @RequestBody EmailRequest emailRequest) {
        try {
            amazonSESService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email sent successfully via Amazon SES!");
            response.put("status", "success");
            response.put("method", "Amazon SES");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to send email via Amazon SES");
            response.put("status", "error");
            response.put("method", "Amazon SES");
            return ResponseEntity.badRequest().body(response);
        }
    }
}


