package ru.Frozik6k.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.model.kafka.UserOperation;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final MailService mail;

    @KafkaListener(topics = "${app.kafka.topics.user-events:user.events}", groupId = "group_id")
    public void onEvent(UserEvent userEvent) {
        if (userEvent == null) {
            log.warn("Received null user event");
            return;
        }

        String email = userEvent.email();
        if (email == null) {
            log.warn("Received user event without email: {}", userEvent);
            return;
        }

        UserOperation operation = userEvent.userOperation();
        if (operation == null) {
            log.warn("Received user event without operation: {}", userEvent);
            return;
        }

        switch (operation) {
            case CREATED -> mail.sendAccountCreated(email);
            case DELETED -> mail.sendAccountDeleted(email);
            default -> log.warn("Received unsupported user operation: {}", operation);
        }
    }
}
