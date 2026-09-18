package ru.rsl.api.web.dto;

import java.util.List;

public record ProgressResponse(
        int themesUnlocked,
        int themesCompleted,
        int lessonsCompleted,
        List<ThemeResponse> themes
) {
}
