package ru.rsl.api.web.dto;

import ru.rsl.api.Model.CheckpointOption;
import ru.rsl.api.Model.Gesture;

import java.util.UUID;

public record CheckpointOptionResponse(UUID id, UUID gestureId, String gestureName, String videoUrl) {

    public static CheckpointOptionResponse from(CheckpointOption option) {
        Gesture gesture = option.getGesture();
        return new CheckpointOptionResponse(
                option.getId(),
                gesture.getId(),
                gesture.getName(),
                gesture.getVideoUrl()
        );
    }
}
