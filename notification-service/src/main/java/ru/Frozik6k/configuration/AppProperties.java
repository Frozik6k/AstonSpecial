package ru.Frozik6k.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final Mail mail = new Mail();
    private final Site site = new Site();

    @Getter
    public static class Mail {
        private String from;
    }

    @Getter
    public static class Site {
        private String name;
    }
}
