package com.example.cargarage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cargarage.admin.service.EmailService;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    // ✅ This will use Spring's JavaMailSender configuration
    @Test
    public void testSendMail() {
        String to = "email@gmail.com";
        String subject = "Manual Email Test";
        String body = "This is a test using Spring Boot context.";

        emailService.sendEmail(to, subject, body);
    }
}
