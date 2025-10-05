package ru.Frozik6k.model.kafka;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserEvent(
        @NotNull(message = "Операция не найдена")
        UserOperation userOperation,
        @NotBlank(message = "Почта не указана")
        @Email(message = "не правильный формат почты")
        String email
) {
}
