package ru.Frozik6k.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Frozik6k.dao.UserDao;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDao, userMapper);
    }

    @Test
    void create_shouldMapDtoAndSaveUser() {
        String name = "Петров Александр Сергеевич";
        String email = "petrov@temp.ru";
        Integer age = 34;
        UserDto dto = new UserDto(name, email, age);
        User user = new User(name, email, age);
        when(userMapper.toUser(dto)).thenReturn(user);
        when(userDao.create(user)).thenReturn(1L);

        Long id = userService.create(dto);

        assertEquals(1L, id);
        verify(userMapper).toUser(dto);
        verify(userDao).create(user);
    }

    @Test
    void read_shouldReturnDtoIfExists() {
        Long id = 1L;
        String name = "Петров Александр Сергеевич";
        String email = "petrov@temp.ru";
        Integer age = 34;
        UserDto dto = new UserDto(name, email, age);
        User user = new User(name, email, age);
        when(userDao.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        Optional<UserDto> result = userService.read(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(userDao).findById(id);
        verify(userMapper).toDto(user);
    }

    @Test
    void read_shouldReturnEmptyIfNotFound() {
        Long id = 1L;
        when(userDao.findById(id)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.read(id);

        assertTrue(result.isEmpty());
        verify(userDao).findById(id);
        verifyNoInteractions(userMapper);
    }

    @Test
    void readUsers_shouldReturnMappedDtos() {
        String name1 = "Петров Александр Сергеевич";
        String email1 = "petrov@temp.ru";
        Integer age1 = 34;
        String name2 = "Александрова Алевтина Григорьевна";
        String email2 = "alex@temp.ru";
        Integer age2 = 32;

        User user1 = new User(name1, email1, age1);
        User user2 = new User(name2, email2, age2);
        UserDto dto1 = new UserDto(name1, email1, age1);
        UserDto dto2 = new UserDto(name2, email2, age2);
        when(userDao.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(any(User.class))).thenReturn(dto1, dto2);

        List<UserDto> result = userService.readUsers();

        assertEquals(List.of(dto1, dto2), result);
        verify(userDao).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void update_shouldDelegateToDao() {
        String name = "Петров Александр Сергеевич";
        String email = "petrov@temp.ru";
        Integer age = 34;
        User user = new User(name, email, age);

        userService.update(user);

        verify(userDao).update(user);
        verifyNoInteractions(userMapper);
    }

    @Test
    void delete_shouldDelegateToDao() {
        Long id = 1L;

        userService.delete(id);

        verify(userDao).deleteById(id);
        verifyNoInteractions(userMapper);
    }
}
