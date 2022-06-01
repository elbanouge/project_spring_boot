package com.project.request_credit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("laylaelhajjaji@gmail.com ");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("mail sent ............");
        return true;
    }


    public boolean sendEmailToAdmin(String fromEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo("laylaelhajjaji@gmail.com ");
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("mail sent to laylaelhajjaji@gmail.com ................");
        return true;
    }

}