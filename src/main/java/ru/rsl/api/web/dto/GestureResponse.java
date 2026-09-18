package ru.rsl.api.web.dto;

import ru.rsl.api.Model.Gesture;

import java.util.UUID;

public record GestureResponse(UUID id, String name, String description, String videoUrl) {

    public static GestureResponse from(Gesture gesture) {
        return new GestureResponse(
                gesture.getId(),
                gesture.getName(),
                gesture.getDescription(),
                gesture.getVideoUrl()
        );
    }
}
