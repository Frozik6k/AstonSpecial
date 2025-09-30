package ru.Frozik6k.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.service.UserService;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Добавление нового пользователя")
    public ResponseEntity<Void> addUser(@RequestBody UserDto userDto) {
        userService.add(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о пользователя по id")
    public EntityModel<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = userService.getUser(id);
        return toModel(userDto);
    }

    @GetMapping
    @Operation(summary = "Получить список всех пользователей")
    public CollectionModel<EntityModel<UserDto>> getUsers() {
        List<EntityModel<UserDto>> users = userService.getUsers().stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
    }

    @PutMapping("/{id}")
    @Operation(summary = "обновить информацию о пользователе")
    public ResponseEntity<Void> editUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    private EntityModel<UserDto> toModel(UserDto userDto) {
        Long userId = Objects.requireNonNull(userDto.id(), "User id is required to build links");

        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUser(userId)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUsers()).withRel("users"));
    }
}
