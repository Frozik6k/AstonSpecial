package ru.Frozik6k.mapper;

import org.mapstruct.Mapper;
import ru.Frozik6k.dto.UserDto;
import ru.Frozik6k.model.kafka.UserEvent;
import ru.Frozik6k.model.kafka.UserOperation;

@Mapper(componentModel = "spring")
public interface UserEventMapper {

    UserEvent toUserEvent(UserDto userDto, UserOperation userOperation);
}
