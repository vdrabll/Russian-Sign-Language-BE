package ru.rsl.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.repo.AppUserRepository;
import ru.rsl.api.web.dto.CreateUserRequest;
import ru.rsl.api.web.dto.UserResponse;

@Service
public class UserService {

    private final AppUserRepository users;

    public UserService(AppUserRepository users) {
        this.users = users;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        var user = new AppUser(request.name().trim(), request.type());
        return UserResponse.from(users.save(user));
    }
}
