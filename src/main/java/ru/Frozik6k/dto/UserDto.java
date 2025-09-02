package ru.Frozik6k.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Frozik6k.model.User;

public record UserDto(
        String name,
        String email,
        String age
) {
}
