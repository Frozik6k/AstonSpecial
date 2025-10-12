package ru.Frozik6k;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.Frozik6k.repository.UserRepository;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration," +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
                "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration",
        "spring.cloud.config.enabled=false",
        "spring.cloud.discovery.enabled=false"
})
public class UserServiceAppTests {

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }
}
