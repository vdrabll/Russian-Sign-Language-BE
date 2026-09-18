package ru.rsl.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.rsl.api.service.UserService;
import ru.rsl.api.web.dto.CreateUserRequest;
import ru.rsl.api.web.dto.UserResponse;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя (hearing / deaf)")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return users.create(request);
    }
}
