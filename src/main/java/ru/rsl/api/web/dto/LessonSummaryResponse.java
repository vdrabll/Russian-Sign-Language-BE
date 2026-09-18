package ru.rsl.api.web.dto;

import ru.rsl.api.Model.Lesson;

import java.util.UUID;

public record LessonSummaryResponse(
        UUID id,
        String title,
        String description,
        int sortOrder,
        boolean completed
) {

    public static LessonSummaryResponse from(Lesson lesson, boolean completed) {
        return new LessonSummaryResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getSortOrder(),
                completed
        );
    }
}
