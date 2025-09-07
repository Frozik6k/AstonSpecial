package ru.Frozik6k.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ru.Frozik6k.model.User;
import ru.Frozik6k.utility.HibernateUtility;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public Long create(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtility.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Long id = (Long) session.save(user);
            tx.commit();
            log.info("Created user id={}", id);
            return id;
        } catch (HibernateException exception) {
            if (tx != null) tx.rollback();
            log.error("Error creating user", exception);
            throw exception;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtility.sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (HibernateException exception) {
            log.error("Error fetching user id={}", id, exception);
            throw exception;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtility.sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (HibernateException exception) {
            log.error("Error fetching all users", exception);
            throw exception;
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtility.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            log.info("Updated user id={}", user.getId());
        } catch (HibernateException exception) {
            if (tx != null) tx.rollback();
            log.error("Error updating user id={}", user.getId(), exception);
            throw exception;
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtility.sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                log.info("Deleted user id={}", id);
            } else {
                log.warn("User id={} not found, nothing to delete", id);
            }
            tx.commit();
        } catch (HibernateException exception) {
            if (tx != null) tx.rollback();
            log.error("Error deleting user id={}", id, exception);
            throw exception;
        }
    }
}
