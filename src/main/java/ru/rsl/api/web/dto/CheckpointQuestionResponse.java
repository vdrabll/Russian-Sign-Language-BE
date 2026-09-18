package ru.rsl.api.web.dto;

import java.util.List;
import java.util.UUID;

public record CheckpointQuestionResponse(
        UUID id,
        String prompt,
        int sortOrder,
        List<CheckpointOptionResponse> options
) {
}
