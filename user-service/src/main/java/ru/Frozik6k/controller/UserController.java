package ru.Frozik6k.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Frozik6k.dto.UserDto;

@Tag(name = "Users", description = "API для операций над учетными записями пользователей")
public interface UserController {

    @Operation(summary = "Добавление нового пользователя", tags = "addUser")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Добавление новой учетной записи пользователя",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    }
            )
    })
    ResponseEntity<Void> addUser(@RequestBody UserDto userDto);

    @Operation(summary = "Получить информацию о пользователя по id", tags = "getUser")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение информации о пользователе с индификатором id",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    }
            )
    })
    EntityModel<UserDto> getUser(@PathVariable Long id);

    @Operation(summary = "Получить список всех пользователей", tags = "getUsers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение списка всех пользователей",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    }
            )
    })
    CollectionModel<EntityModel<UserDto>> getUsers();

    @Operation(summary = "обновить информацию о пользователе", tags = "editUser")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изменение информации о пользователе",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    }
            )
    })
    ResponseEntity<Void> editUser(@RequestBody UserDto userDto);

    @Operation(summary = "Удалить пользователя", tags = "delete")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удаляет пользователя из базы данных",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    }
            )
    })
    ResponseEntity<Void> delete(@PathVariable Long id);

}
