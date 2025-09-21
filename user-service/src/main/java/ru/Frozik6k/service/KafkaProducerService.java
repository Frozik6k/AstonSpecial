package ru.Frozik6k.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.Frozik6k.model.kafka.Topics;
import ru.Frozik6k.model.kafka.UserEvent;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, UserEvent> kafkaTemplate;


    public KafkaProducerService(KafkaTemplate<String, UserEvent> kafkaTemplate) {}

    public void sendMessage(UserEvent userEvent) {
        kafkaTemplate.send(Topics.USER_EVENTS, userEvent);
    }
}
