package ru.Frozik6k.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @Schema(description = "Имя пользователя")
        @NotBlank
        String name,

        @Schema(description = "почта пользователя")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Возраст пользователя")
        Integer age
) {
}
