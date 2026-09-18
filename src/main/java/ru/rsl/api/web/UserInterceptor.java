package ru.rsl.api.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.rsl.api.repo.AppUserRepository;

import java.util.UUID;

@Component
public class UserInterceptor implements HandlerInterceptor {

    private final AppUserRepository users;

    public UserInterceptor(AppUserRepository users) {
        this.users = users;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String raw = request.getHeader(CurrentUser.HEADER);
        if (raw == null || raw.isBlank()) {
            throw ApiException.unauthorized("Нужен заголовок " + CurrentUser.HEADER);
        }
        UUID id;
        try {
            id = UUID.fromString(raw.trim());
        } catch (IllegalArgumentException ex) {
            throw ApiException.badRequest("X-User-Id должен быть UUID");
        }
        var user = users.findById(id)
                .orElseThrow(() -> ApiException.unauthorized("Пользователь не найден"));
        request.setAttribute(CurrentUser.ATTR, user);
        return true;
    }
}
