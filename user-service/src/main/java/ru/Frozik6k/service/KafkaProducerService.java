package ru.Frozik6k.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.Frozik6k.model.kafka.UserEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Value("${app.kafka.topics.user-events:user.events}")
    private String userEventsTopic;

    public void sendMessage(UserEvent userEvent) {
        log.info("Отправка в Kafka");
        kafkaTemplate.send(userEventsTopic, userEvent);
    }
}
