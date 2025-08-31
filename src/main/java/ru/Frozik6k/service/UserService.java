package ru.Frozik6k.service;

import ru.Frozik6k.dao.UserDao;
import ru.Frozik6k.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserService {

    private final UserDao userDao;
    private final Scanner scanner;

    public UserService(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void addUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine().trim();
        System.out.println("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Возраст (целое число или пусто): ");
        String ageStr = scanner.nextLine().trim();
        Integer age = ageStr.isEmpty() ? null : Integer.parseInt(ageStr);

        User user = new User(name, email, age);
        Long id = userDao.create(user);
        System.out.println("Создан пользователь с ID = " + id);
    }

    public User getUser() {
        System.out.print("ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<User> opt = userDao.findById(id);
        System.out.println(opt.map(User::toString).orElse("Пользователь не найден"));
        User user = opt.get();
        return user;
    }

    public List<User> getUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            users.forEach(System.out::println);
        }
        return users;
    }

    public User editUser() {
        System.out.print("ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<User> opt = userDao.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Пользователь не найден");
            return null;
        }
        User user = opt.get();

        System.out.print("Новое имя (пусто — оставить \"" + user.getName() + "\"): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) user.setName(name);

        System.out.print("Новый email (пусто — оставить \"" + user.getEmail() + "\"): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Новый возраст (пусто — оставить \"" + user.getAge() + "\"): ");
        String ageStr = scanner.nextLine().trim();
        if (!ageStr.isEmpty()) user.setAge(Integer.parseInt(ageStr));

        userDao.update(user);
        System.out.println("Пользователь обновлён.");
        return user;
    }

    public void deleteUser() {
        System.out.print("ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userDao.deleteById(id);
        System.out.println("Если пользователь существовал — он удалён.");
    }
}
