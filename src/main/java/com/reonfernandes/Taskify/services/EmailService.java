package com.reonfernandes.Taskify.services;

public interface EmailService {
    void sendEmail(String recipient, String subject, String message);
}
