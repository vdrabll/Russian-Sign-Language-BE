package ru.rsl.api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.rsl.api.Model.UserType;

public record CreateUserRequest(
        @NotBlank String name,
        @NotNull UserType type
) {
}
