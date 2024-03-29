package com.codingdrama.hrsystem.service.email.service;

import com.codingdrama.hrsystem.exceptions.LocalizedResponseStatusException;
import com.codingdrama.hrsystem.service.email.context.AbstractEmailContext;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {
    
    private JavaMailSender javaMailSender;
    private SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendMail(AbstractEmailContext email) {
        MimeMessage message = prepareEmail(javaMailSender, email);
        javaMailSender.send(message);
    }
    

    private MimeMessage prepareEmail(JavaMailSender javaMailSender, AbstractEmailContext email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(email.getContext());
            String emailContent = templateEngine.process(email.getTemplateLocation(), context);

            mimeMessageHelper.setTo(email.getTo());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setFrom(email.getFrom());
            mimeMessageHelper.setText(emailContent, true);
            mimeMessageHelper.addInline("bdacs-logo.jpg", new ClassPathResource("templates/emails/bdacs-logo.jpg"));


            return message;
        } catch (MessagingException e) {
            throw new LocalizedResponseStatusException(HttpStatus.BAD_REQUEST, "message.was.not.sent");
        }
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
}
