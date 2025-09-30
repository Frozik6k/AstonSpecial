package ru.Frozik6k.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.Frozik6k.configuration.AppProperties;
import ru.Frozik6k.model.kafka.UserEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final AppProperties appProperties;
    private final JavaMailSender mailSender;

    public void sendAccount(UserEvent userEvent) {
        switch (userEvent.userOperation()) {
            case CREATED -> sendAccountCreated(userEvent.email());
            case DELETED -> sendAccountDeleted(userEvent.email());
        }
    }

    public void sendAccountCreated(String toEmail) {
        String siteName = appProperties.getSite().getName();
        send(toEmail, "Аккаунт", "Здравствуйте! Ваш аккаунт на сайте " + siteName + " был успешно создан.");
        log.info("Sending account created email to " + toEmail);
    }

    public void sendAccountDeleted(String toEmail) {
        send(toEmail, "Аккаунт", "Здравствуйте! Ваш аккаунт был удален.");
        log.info("Sending account deleted email to " + toEmail);
    }

    private void send(String toEmail, String subject, String text) {
        var msg = new SimpleMailMessage();
        String fromEmail = appProperties.getMail().getFrom();
        msg.setFrom(fromEmail);
        msg.setTo(toEmail);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}
