package ru.Frozik6k.dto;

public record UserDto(
        String name,
        String email,
        Integer age
) {
    @Override
    public String toString() {
        return "Пользователь{" +
                "Имя=" + name +
                ", email=" + email +
                ", Возраст=" + age +
                '}';
    }
}
