package ru.rsl.api.web.dto;

import java.util.UUID;

public record GestureProgressResponse(
        UUID id,
        String name,
        String description,
        String videoUrl,
        boolean practiced
) {
}
