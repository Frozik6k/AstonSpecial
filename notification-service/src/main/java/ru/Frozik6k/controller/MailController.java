package ru.Frozik6k.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.Frozik6k.model.kafka.UserEvent;

@Tag(name = "Mail notifications", description = "API для отправки уведомплений на почту пользователя")
public interface MailController {
    @Operation(summary = "Отправить сообщение на почту пользователю о создании аккаунта", tags = "sendMailCreateAccount")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отправка сообщения пользователю о создании аккаунта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserEvent.class))
                            )
                    }
            )
    })
    ResponseEntity<Void> sendMailCreateAccount(@RequestBody UserEvent userEvent);
}
