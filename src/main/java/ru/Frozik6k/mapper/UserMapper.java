package ru.Frozik6k.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.model.User;

@Mapper
public interface UserMapper {
    UserDto toDto(User entity);

    User toUser(UserDto userDto);
}
