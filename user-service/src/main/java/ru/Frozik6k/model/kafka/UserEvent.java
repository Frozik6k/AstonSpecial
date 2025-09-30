package ru.Frozik6k.model.kafka;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserEvent(
        @NotNull
        UserOperation userOperation,
        @NotBlank
        @Email
        String email
) {
}
