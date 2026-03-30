package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.exceptions.services.EmailSenderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.mail.properties.sender.timeout}")
    private Integer timeout;

    private final JavaMailSender emailSender;

    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String body) {
        return CompletableFuture.runAsync(() -> {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(emailFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            emailSender.send(message);
        })
        .orTimeout(timeout, TimeUnit.SECONDS)
        .handle((result, throwable) -> {
            log.error("Falhou ao enviar o e-mail", throwable);
            throw new EmailSenderException("Falhou ao enviar o e-mail");
        });
    }

}
