package ru.Frozik6k.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.exception.UserNotFoundException;
import ru.Frozik6k.mapper.UserEventMapper;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.model.User;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.model.kafka.UserOperation;
import ru.Frozik6k.repository.UserRepository;
import ru.Frozik6k.service.KafkaProducerService;
import ru.Frozik6k.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventMapper userEventMapper;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public Long add(UserDto userDto) {
        UserEvent userEvent = userEventMapper.toUserEvent(userDto, UserOperation.CREATED);
        long idUser = userRepository.save(userMapper.toUser(userDto)).getId();
        log.info("Перед отправкой в кафку");
        kafkaProducerService.sendMessage(userEvent);
        log.info("Сообщение через кафку уже отправлено");
        return idUser;
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
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        UserEvent userEvent = new UserEvent(UserOperation.DELETED, user.getEmail());
        userRepository.deleteById(id);
        log.info("Перед отправкой в кафку");
        kafkaProducerService.sendMessage(userEvent);
        log.info("Сообщение через кафку уже отправлено");
    }
}
