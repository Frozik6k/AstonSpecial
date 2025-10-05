package ru.Frozik6k.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "userEventsProducer", fallbackMethod = "sendMessageFallback")
    public void sendMessage(UserEvent userEvent) {
        log.info("Отправка в Kafka");
        kafkaTemplate.send(userEventsTopic, userEvent);
    }

    @SuppressWarnings("unused")
    private void sendMessageFallback(UserEvent userEvent, Throwable throwable) {
        log.error("Не удалось отправить сообщение в Kafka для пользователя {}. Сообщение помещено в журнал.",
                userEvent.email(), throwable);
    }
}
