package ru.Frozik6k;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.Frozik6k.dao.UserDao;
import ru.Frozik6k.dao.UserDaoImpl;
import ru.Frozik6k.service.UserService;
import ru.Frozik6k.utility.HibernateUtility;

import java.util.Scanner;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("User Service started");
        UserDao userDao = new UserDaoImpl();
        Scanner scanner = new Scanner(System.in);

        UserService userService = new UserService(userDao, scanner);

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Выберите пункт меню: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1" -> userService.create();
                    case "2" -> userService.read();
                    case "3" -> userService.readUsers();
                    case "4" -> userService.update();
                    case "5" -> userService.delete();
                    case "0" -> running = false;
                    default -> System.out.println("Неизвестная команда.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                log.error("Unexpected error", e);
            }
            System.out.println();
        }

        HibernateUtility.shutdown();
        System.out.println("Пока!");
        log.info("User Service stopped");


    }

    private static void printMenu() {
        System.out.println("""
                *** User Service ***
                1. Создать пользователя
                2. Найти пользователя по ID
                3. Показать всех пользователей
                4. Обновить пользователя
                5. Удалить пользователя
                0. Выход
                """);
    }

}
