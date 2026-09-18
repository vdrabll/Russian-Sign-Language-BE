package ru.rsl.api.web;

import ru.rsl.api.Model.AppUser;

public final class CurrentUser {

    public static final String HEADER = "X-User-Id";
    public static final String ATTR = "currentUser";

    private CurrentUser() {
    }

    public static AppUser require(AppUser user) {
        if (user == null) {
            throw ApiException.unauthorized("Нужен заголовок " + HEADER);
        }
        return user;
    }
}
