package ru.rsl.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.service.CourseService;
import ru.rsl.api.web.dto.ThemeResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/themes")
@Tag(name = "Themes")
@SecurityRequirement(name = "userId")
public class ThemeController {

    private final CourseService course;

    public ThemeController(CourseService course) {
        this.course = course;
    }

    @GetMapping
    @Operation(summary = "Список тем с флагом unlocked")
    public List<ThemeResponse> list(AppUser user) {
        return course.listThemes(CurrentUser.require(user));
    }

    @GetMapping("/{themeId}")
    @Operation(summary = "Тема и её уроки")
    public ThemeResponse get(AppUser user, @PathVariable UUID themeId) {
        return course.getTheme(CurrentUser.require(user), themeId);
    }
}
