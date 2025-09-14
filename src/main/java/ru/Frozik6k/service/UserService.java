package ru.Frozik6k.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.Frozik6k.repository.UserRepository;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.model.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Long add(UserDto userDto);

    UserDto getUser(Long id);

    List<UserDto> getUsers();

    void editUser(Long id, UserDto userDto) throws Exception;

    void deleteUser(Long id);
}
