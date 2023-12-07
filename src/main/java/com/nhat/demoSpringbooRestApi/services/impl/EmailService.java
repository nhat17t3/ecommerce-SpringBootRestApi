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
        message.setFrom("hoanglongnhat0605@gmail.com");
        message.setTo(toEmail);
        message.setSubject("demospring boot");
        message.setText("test mailer demo");
        javaMailSender.send(message);
    }

    public void sendEmailFromTemplate(String toEmail, String templateName, String subject, Object data) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom("hoanglongnhat0605@gmail.com");
        messageHelper.setTo("hoanglongnhat0605@gmail.com");
        messageHelper.setSubject(subject);

        Context context = new Context();
        context.setVariable("data", data);
        String content = templateEngine.process(templateName, context);
        messageHelper.setText(content, true);

        javaMailSender.send(mimeMessage);
    }
}