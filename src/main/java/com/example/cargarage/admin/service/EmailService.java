package com.example.cargarage.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

    // ✅ Constructor injection (cleanest approach)
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // ✅ Method to send email
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail); // Send the email
        } catch (Exception e) {
            handleEmailException(e, to, subject);
        }
    }

    // ✅ Handle email exceptions
    private void handleEmailException(Exception e, String to, String subject) {
        System.err.println("❌ Failed to send email to: " + to + " | Subject: " + subject);
        System.err.println("🔍 Error: " + e.getMessage());
        throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
    }
}
