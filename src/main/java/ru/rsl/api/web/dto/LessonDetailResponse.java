package ru.rsl.api.web.dto;

import ru.rsl.api.Model.Lesson;

import java.util.List;
import java.util.UUID;

public record LessonDetailResponse(
        UUID id,
        UUID themeId,
        String title,
        String description,
        String videoUrl,
        int sortOrder,
        boolean videoWatched,
        boolean completed,
        List<GestureProgressResponse> gestures
) {

    public static LessonDetailResponse from(
            Lesson lesson,
            boolean videoWatched,
            boolean completed,
            List<GestureProgressResponse> gestures
    ) {
        return new LessonDetailResponse(
                lesson.getId(),
                lesson.getTheme().getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getVideoUrl(),
                lesson.getSortOrder(),
                videoWatched,
                completed,
                gestures
        );
    }
}
