package ru.Frozik6k.model.kafka;

public record UserEvent(
        UserOperation userOperation,
        String email
) {
}
