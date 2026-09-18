package ru.rsl.api.web.dto;

import ru.rsl.api.Model.AppUser;
import ru.rsl.api.Model.UserType;

import java.util.UUID;

public record UserResponse(UUID id, String name, UserType type) {

    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getName(), user.getUserType());
    }
}
