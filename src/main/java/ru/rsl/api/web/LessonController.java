package ru.rsl.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.service.CourseService;
import ru.rsl.api.web.dto.LessonDetailResponse;
import ru.rsl.api.web.dto.PracticeRequest;
import ru.rsl.api.web.dto.PracticeResponse;

import java.util.UUID;

@RestController
@RequestMapping("/lessons")
@Tag(name = "Lessons")
@SecurityRequirement(name = "userId")
public class LessonController {

    private final CourseService course;

    public LessonController(CourseService course) {
        this.course = course;
    }

    @GetMapping("/{lessonId}")
    @Operation(summary = "Урок: видео и жесты")
    public LessonDetailResponse get(AppUser user, @PathVariable UUID lessonId) {
        return course.getLesson(CurrentUser.require(user), lessonId);
    }

    @PostMapping("/{lessonId}/watch")
    @Operation(summary = "Отметить обучающее видео просмотренным")
    public LessonDetailResponse watch(AppUser user, @PathVariable UUID lessonId) {
        return course.watch(CurrentUser.require(user), lessonId);
    }

    @PostMapping("/{lessonId}/practice")
    @Operation(summary = "Отправить запись жеста. В URL добавь fail или unclear, чтобы получить ошибку")
    public PracticeResponse practice(
            AppUser user,
            @PathVariable UUID lessonId,
            @Valid @RequestBody PracticeRequest request
    ) {
        return course.practice(CurrentUser.require(user), lessonId, request);
    }
}
