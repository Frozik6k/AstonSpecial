package ru.Frozik6k;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.Frozik6k.configuration.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@OpenAPIDefinition
public class NotificationServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApp.class, args);
    }
}
