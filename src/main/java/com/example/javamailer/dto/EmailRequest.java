package com.example.javamailer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Email request details")
public class EmailRequest {
    
    @Schema(description = "Recipient email address", example = "recipient@example.com", required = true)
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String to;
    
    @Schema(description = "Email subject line", example = "Test Email", maxLength = 200, required = true)
    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;
    
    @Schema(description = "Email body content", example = "This is a test email", maxLength = 10000, required = true)
    @NotBlank(message = "Email body is required")
    @Size(max = 10000, message = "Email body must not exceed 10000 characters")
    private String body;

    // Constructors
    public EmailRequest() {}

    public EmailRequest(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    // Getters and setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}