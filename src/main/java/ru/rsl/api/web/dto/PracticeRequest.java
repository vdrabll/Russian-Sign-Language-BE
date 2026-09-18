package ru.rsl.api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PracticeRequest(@NotNull UUID gestureId, @NotBlank String recordingUrl) {
}
