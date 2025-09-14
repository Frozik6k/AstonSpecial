package ru.Frozik6k.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/")
    public Long addUser(@RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity editUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            userService.editUser(id, userDto);
        } catch (Exception exception) {
            log.error("Error editing user", exception);
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("User deleted: {}", id);
    }
}
