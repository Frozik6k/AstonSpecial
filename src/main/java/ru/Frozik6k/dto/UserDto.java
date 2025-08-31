package ru.Frozik6k.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.Frozik6k.model.User;

@Data
@NoArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private Integer age;

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

    public UserDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
    }
}
