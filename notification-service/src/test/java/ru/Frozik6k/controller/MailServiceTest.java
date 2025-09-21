package ru.Frozik6k.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.model.kafka.UserOperation;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.atLeastOnce;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.verify;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.Frozik6k.service.KafkaConsumerService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
public class MailServiceTest {
    protected static final String TOPIC = "user.events";
    static final DockerImageName KAFKA_IMAGE =
            DockerImageName.parse("apache/kafka:3.7.1");
    @org.testcontainers.junit.jupiter.Container
    @ServiceConnection
    static final KafkaContainer KAFKA = new KafkaContainer(KAFKA_IMAGE);

    @MockitoSpyBean
    KafkaConsumerService listener; // шпион на реальном бине-слушателе

    @BeforeAll
    void start() throws Exception {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        try (AdminClient admin = AdminClient.create(props)) {
            try {
                admin.createTopics(List.of(new NewTopic(TOPIC, 1, (short) 1))).all().get();
            } catch (ExecutionException e) {
                if (!(e.getCause() instanceof TopicExistsException)) {
                    throw e; // остальные ошибки — пробрасываем
                }
                // если топик уже есть — ок, продолжаем
            }
        }
    }

    @AfterAll
    void stop() {
        KAFKA.stop();
    }



    @Test
    void whenJsonWithCreatedSent_thenListenerReceivesUserEvent() {
        String email = "created@example.com";
        String json = """
      {"userOperation":"CREATED","email":"%s"}
      """.formatted(email);

        // Отправляем сообщение
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(p)) {
            producer.send(new ProducerRecord<>(TOPIC, json)); // ВАЖНО: дождаться отправки
        }

        // Ждём вызов метода @KafkaListener
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            var captor = ArgumentCaptor.forClass(UserEvent.class);
            verify(listener, atLeastOnce()).onEvent(captor.capture());
            UserEvent ev = captor.getValue();
            assertThat(ev.email()).isEqualTo(email);
            assertThat(ev.userOperation()).isEqualTo(UserOperation.CREATED);
        });
    }

}
