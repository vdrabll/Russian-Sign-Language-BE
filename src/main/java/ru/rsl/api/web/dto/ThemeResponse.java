package ru.rsl.api.web.dto;

import java.util.List;
import java.util.UUID;

public record ThemeResponse(
        UUID id,
        String title,
        String description,
        int sortOrder,
        boolean unlocked,
        boolean completed,
        boolean checkpointPassed,
        List<LessonSummaryResponse> lessons
) {
}
