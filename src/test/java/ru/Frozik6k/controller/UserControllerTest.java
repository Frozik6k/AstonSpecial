package ru.Frozik6k.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.mapper.UserMapper;
import ru.Frozik6k.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Test
    @DisplayName("Проверяем запрос на добавление User")
    void addUserReturnsId() throws Exception {
        UserDto userDto = new UserDto("Андрей", "andrei@frozik.ru", 51);
        given(userService.add(any(UserDto.class))).willReturn(7L);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));

        verify(userService).add(userDto);
    }

    @Test
    @DisplayName("Проверяем запрос на получение информации о User")
    void getUserReturnsUserDto() throws Exception {
        UserDto userDto = new UserDto("Максим", "max@frozik.ru", 33);
        given(userService.getUser(28L)).willReturn(userDto);

        mockMvc.perform(get("/users/28"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @DisplayName("Проверяем запрос на получение всех User")
    void getUsersReturnsList() throws Exception {
        List<UserDto> userDtos = List.of(
                new UserDto("Андрей", "andrei@frozik.ru", 51),
                new UserDto("Максим", "max@frozik.ru", 33)
        );
        given(userService.getUsers()).willReturn(userDtos);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDtos)));
    }

    @Test
    @DisplayName("Проверяем запрос на обновление данных User")
    void editUserUpdatesUser() throws Exception {
        UserDto userDto = new UserDto("Максим", "max777@frozik6k", 34);
        willDoNothing().given(userService).editUser(eq(28L), any(UserDto.class));

        mockMvc.perform(patch("/users/28")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        verify(userService).editUser(28L, userDto);
    }

    @Test
    @DisplayName("Проверяем запрос на попытку обновления данных User, если такого не существует")
    void editUser_whenServiceThrows_returnsBadRequest() throws Exception {
        UserDto userDto = new UserDto("Максим", "max777@frozik6k", 34);
        doThrow(new Exception("User not found")).when(userService).editUser(eq(28L), any(UserDto.class));

        mockMvc.perform(patch("/users/28")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    @DisplayName("Проверяем запрос на удаление User")
    void deleteUserRemovesUser() throws Exception {
        willDoNothing().given(userService).deleteUser(33L);

        mockMvc.perform(delete("/users/33"))
                .andExpect(status().isOk());

        verify(userService).deleteUser(33L);
    }
}