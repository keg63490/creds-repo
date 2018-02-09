package com.example.credsrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class Mailer{

    @Autowired
    public JavaMailSender emailSender;

    public void sendMail(String to, String subject, String msg) {
        //creating message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setFrom("ucmoseniorproject@outlook.com");
        message.setText(msg);
        emailSender.send(message);
    }
}
