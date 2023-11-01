package com.nhat.demoSpringbooRestApi.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;


@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleEmail(String toEmail) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("globalSetting.getRIS().getFullEmailAccount()");
        message.setTo("email.getMultipleRecipient()");
        message.setSubject("email.getSubject()");
        message.setText("email.getContent()");
        javaMailSender.send(message);
    }

    public void sendEmailFromTemplate(String to, String templateName, String subject, String message) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom("noreply@example.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);

        Context context = new Context();
        context.setVariable("message", message);
        String content = templateEngine.process("mail-order", context);
        messageHelper.setText(content, true);

        javaMailSender.send(mimeMessage);
    }
}