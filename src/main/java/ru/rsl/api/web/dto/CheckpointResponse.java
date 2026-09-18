package ru.rsl.api.web.dto;

import java.util.List;
import java.util.UUID;

public record CheckpointResponse(
        UUID id,
        UUID themeId,
        String title,
        int passPercent,
        boolean passed,
        List<CheckpointQuestionResponse> questions
) {
}
