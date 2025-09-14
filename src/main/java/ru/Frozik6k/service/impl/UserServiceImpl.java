package ru.Frozik6k.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.model.User;
import ru.Frozik6k.repository.UserRepository;
import ru.Frozik6k.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Long add(UserDto userDto) {
        return userRepository.save(
                userMapper.toUser(userDto)
        ).getId();
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow();
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .toList();
        return userDtos;
    }

    @Override
    public void editUser(Long id, UserDto userDto) throws Exception {
        User user = userMapper.toUser(userDto);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user.setId(id);
            userRepository.save(user);
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
