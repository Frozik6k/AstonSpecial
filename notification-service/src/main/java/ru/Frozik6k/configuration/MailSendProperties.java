package ru.Frozik6k.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mailsend")
@Getter
@Setter
public class MailSendProperties {
    private String created;
    private String subject;
    private String deleted;

}
