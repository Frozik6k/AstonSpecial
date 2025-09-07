package ru.Frozik6k.dao;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.Frozik6k.model.User;
import ru.Frozik6k.utility.HibernateUtilityTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("users_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static SessionFactory sessionFactory;
    private static UserDao userDao;

    @BeforeAll
    static void initAll() throws Exception {
        POSTGRES.start();

        String url = POSTGRES.getJdbcUrl();
        String user = POSTGRES.getUsername();
        String pass = POSTGRES.getPassword();

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.yaml",
                    new ClassLoaderResourceAccessor(),
                    database
            );
            liquibase.update((String) null);
        }

        sessionFactory = HibernateUtilityTest.buildSessionFactory(url, user, pass);

        userDao = new UserDaoImpl() {
            private final SessionFactory sf = sessionFactory;

            @Override
            public Long create(User user) {
                var session = sf.openSession();
                var tx = session.beginTransaction();
                Long id = (Long) session.save(user);
                tx.commit();
                session.close();
                return id;
            }

            @Override
            public Optional<User> findById(Long id) {
                var session = sf.openSession();
                User u = session.get(User.class, id);
                session.close();
                return Optional.ofNullable(u);
            }

            @Override
            public List<User> findAll() {
                var session = sf.openSession();
                var list = session.createQuery("from User", User.class).list();
                session.close();
                return list;
            }

            @Override
            public void update(User user) {
                var session = sf.openSession();
                var tx = session.beginTransaction();
                session.merge(user);
                tx.commit();
                session.close();
            }

            @Override
            public void deleteById(Long id) {
                var session = sf.openSession();
                var tx = session.beginTransaction();
                User u = session.get(User.class, id);
                if (u != null) session.remove(u);
                tx.commit();
                session.close();
            }
        };
    }

    @AfterAll
    static void tearDownAll() {
        if (sessionFactory != null) sessionFactory.close();
        POSTGRES.stop();
    }

    @Test
    @Order(1)
    void createAndRead() {
        Long id = userDao.create(new User("Alice", "alice@example.com", 30));
        assertNotNull(id);
        var loaded = userDao.findById(id);
        assertTrue(loaded.isPresent());
        assertEquals("Alice", loaded.get().getName());
    }

    @Test
    @Order(2)
    void updateUser() {
        Long id = userDao.create(new User("Bob", "bob@example.com", 25));
        var u = userDao.findById(id).orElseThrow();
        u.setAge(26);
        userDao.update(u);
        assertEquals(26, userDao.findById(id).orElseThrow().getAge());
    }

    @Test
    @Order(3)
    void listAndDelete() {
        Long id = userDao.create(new User("Charlie", "charlie@example.com", null));
        var all = userDao.findAll();
        assertTrue(all.size() >= 1);
        userDao.deleteById(id);
        assertTrue(userDao.findById(id).isEmpty());
    }
}
