package ru.Frozik6k.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.server.core.Relation;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(itemRelation = "user", collectionRelation = "users")
public record UserDto(

        @Schema(description = "Идентификатор пользователя", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

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
