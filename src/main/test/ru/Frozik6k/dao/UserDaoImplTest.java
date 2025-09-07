package ru.Frozik6k.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    static void initAll() {
        POSTGRES.start();

        // Применяем миграции Flyway из classpath: db/migration
        Flyway.configure()
                .dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();

        // Строим SessionFactory для тестовой БД из контейнера
        sessionFactory = TestHibernateUtil.buildSessionFactory(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());

        // Подменяем поведение DAO на версию, использующую sessionFactory теста
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