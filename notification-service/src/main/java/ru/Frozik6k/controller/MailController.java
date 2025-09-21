package ru.Frozik6k.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.service.MailService;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity sendMailCreateAccount(@RequestBody UserEvent userEvent) {
        switch (userEvent.userOperation()) {
            case CREATED -> mailService.sendAccountCreated(userEvent.email());
            case DELETED -> mailService.sendAccountDeleted(userEvent.email());
        }
        return ResponseEntity.ok().build();
    }

}
