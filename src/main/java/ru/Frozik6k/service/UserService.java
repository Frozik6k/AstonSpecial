package ru.Frozik6k.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.Frozik6k.dao.UserDao;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final UserMapper userMapper;

    public UserService(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    public Long create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        Long id = userDao.create(user);
        log.info("Сохранен в БД пользователь с id=" + id);
        return id;
    }

    public Optional<UserDto> read(Long id) {
        return userDao.findById(id)
                .map(userMapper::toDto);
    }

    public List<UserDto> readUsers() {
        return userDao.findAll().stream().map(userMapper::toDto).toList();
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void delete(Long id) {
        userDao.deleteById(id);
    }
}
