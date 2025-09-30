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
    private final MailService mailService;

    @KafkaListener(topics = "${app.kafka.topics.user-events:user.events}", groupId = "group_id")
    public void onEvent(UserEvent userEvent) {
        mailService.sendAccount(userEvent);
    }
}
