package com.reonfernandes.Taskify.services.impl;

import com.reonfernandes.Taskify.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String recipient, String subject, String message) {
        logger.info("(Service) Sending mail to project creator: {}", recipient);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setFrom(senderEmail); // Use the authenticated email
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        emailSender.send(mailMessage);
        logger.info("Email has been sent to project creator: {}", recipient);
    }

}
