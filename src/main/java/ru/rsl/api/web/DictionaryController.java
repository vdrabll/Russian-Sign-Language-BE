package ru.rsl.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rsl.api.service.CourseService;
import ru.rsl.api.web.dto.GestureResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dictionary")
@Tag(name = "Dictionary")
@SecurityRequirement(name = "userId")
public class DictionaryController {

    private final CourseService course;

    public DictionaryController(CourseService course) {
        this.course = course;
    }

    @GetMapping
    @Operation(summary = "Словарь жестов — те же жесты, что в уроках")
    public List<GestureResponse> list(@RequestParam(required = false) String q) {
        return course.dictionary(q);
    }

    @GetMapping("/{gestureId}")
    public GestureResponse get(@PathVariable UUID gestureId) {
        return course.dictionaryItem(gestureId);
    }
}
