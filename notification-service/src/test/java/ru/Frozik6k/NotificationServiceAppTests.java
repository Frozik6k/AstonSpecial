package ru.Frozik6k;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
public class NotificationServiceAppTests {

    @Test
    void contextLoads() {
    }

}
