package ru.Frozik6k.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.Frozik6k.model.kafka.Topics;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.model.kafka.UserOperation;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final MailService mail;

    @KafkaListener(topics = Topics.USER_EVENTS, groupId = "group_id")
    public void onEvent(UserEvent userEvent) {
        if (userEvent == null || userEvent.email() == null) return;
        switch (userEvent.userOperation()) {
            case CREATED: mail.sendAccountCreated(userEvent.email());
            break;
            case DELETED: mail.sendAccountDeleted(userEvent.email());
        }
    }
}
