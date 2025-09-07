package ru.Frozik6k.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.model.User;
import ru.Frozik6k.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Scanner scanner;
    private final UserMapper userMapper;
    private final UserService userService;

    public UserController(Scanner scanner, UserMapper userMapper, UserService userService) {
        this.scanner = scanner;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public void create() {
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();

        System.out.println("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Возраст (целое число или пусто): ");
        String ageStr = scanner.nextLine().trim();
        Integer age = ageStr.isEmpty() ? null : Integer.parseInt(ageStr);

        log.info("Получены данные о пользователе. name=" + name + ", email=" + email + ", age=" + age);

        UserDto userDto = new UserDto(name, email, age);

        Long id = userService.create(userDto);

        System.out.println("Создан пользователь с ID = " + id);
    }

    public void read() {
        System.out.print("ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<UserDto> opt = userService.read(id);

        System.out.println(opt.map(Record::toString).orElse("Пользователь не найден"));
    }

    public void readUsers() {
        List<UserDto> users = userService.readUsers();

        if (users.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            users.forEach(System.out::println);
        }
    }

    public void update() {
        System.out.print("ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<UserDto> opt = userService.read(id);
        if (opt.isEmpty()) {
            System.out.println("Пользователь не найден");
            return;
        }

        User user = userMapper.toUser(opt.get());

        System.out.print("Новое имя (пусто — оставить \"" + user.getName() + "\"): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) user.setName(name);

        System.out.print("Новый email (пусто — оставить \"" + user.getEmail() + "\"): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Новый возраст (пусто — оставить \"" + user.getAge() + "\"): ");
        String ageStr = scanner.nextLine().trim();
        if (!ageStr.isEmpty()) user.setAge(Integer.parseInt(ageStr));

        userService.update(user);
        System.out.println("Пользователь обновлён.");
    }

    public void delete() {
        System.out.print("ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userService.delete(id);
        System.out.println("Если пользователь существовал — он удалён.");
    }
}
