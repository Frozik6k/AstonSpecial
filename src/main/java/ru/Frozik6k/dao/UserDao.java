package ru.Frozik6k.dao;

import ru.Frozik6k.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Long create(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void update(User user);

    void deleteById(Long id);
}
