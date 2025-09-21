package ru.Frozik6k.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    @Value("${app.mail.from}")
    private String fromEmail;
    @Value("${app.site.name}")
    private String siteName;

    public void sendAccountCreated(String toEmail) {
        //send(toEmail, "Аккаунт", "Здравствуйте! Ваш аккаунт на сайте " + siteName + " был успешно создан.");
        log.info("Sending account created email to " + toEmail);
    }

    public void sendAccountDeleted(String toEmail) {
        //send(toEmail, "Аккаунт", "Здравствуйте! Ваш аккаунт был удален.");
        log.info("Sending account deleted email to " + toEmail);
    }

    private void send(String toEmail, String subject, String text) {
        var msg = new SimpleMailMessage();
        msg.setFrom(fromEmail);
        msg.setTo(toEmail);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}
