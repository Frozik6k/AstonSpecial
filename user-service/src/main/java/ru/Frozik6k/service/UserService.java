package ru.Frozik6k.service;

import org.springframework.stereotype.Service;
import ru.Frozik6k.dto.UserDto;

import java.util.List;

@Service
public interface UserService {

    Long add(UserDto userDto);

    UserDto getUser(Long id);

    List<UserDto> getUsers();

    void editUser(UserDto userDto);

    void deleteUser(Long id);
}
