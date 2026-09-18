package ru.rsl.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.service.CourseService;
import ru.rsl.api.web.dto.ProgressResponse;

@RestController
@RequestMapping("/progress")
@Tag(name = "Progress")
@SecurityRequirement(name = "userId")
public class ProgressController {

    private final CourseService course;

    public ProgressController(CourseService course) {
        this.course = course;
    }

    @GetMapping
    @Operation(summary = "Прогресс текущего пользователя")
    public ProgressResponse get(AppUser user) {
        return course.progress(CurrentUser.require(user));
    }
}
