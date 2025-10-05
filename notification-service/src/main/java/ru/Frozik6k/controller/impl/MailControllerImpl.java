package ru.Frozik6k.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Frozik6k.controller.MailController;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.service.MailService;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailControllerImpl implements MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<Void> sendMailCreateAccount(@RequestBody UserEvent userEvent) {
        mailService.sendAccount(userEvent);
        return ResponseEntity.ok().build();
    }
}
