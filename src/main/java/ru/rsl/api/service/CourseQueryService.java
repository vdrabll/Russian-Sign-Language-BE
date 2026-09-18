package ru.rsl.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.Model.CheckpointAttempt;
import ru.rsl.api.Model.Lesson;
import ru.rsl.api.Model.Theme;
import ru.rsl.api.Model.UserLessonProgress;
import ru.rsl.api.repo.CheckpointAttemptRepository;
import ru.rsl.api.repo.ThemeRepository;
import ru.rsl.api.repo.UserLessonProgressRepository;
import ru.rsl.api.web.ApiException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseQueryService {

    private final ThemeRepository themes;
    private final UserLessonProgressRepository progress;
    private final CheckpointAttemptRepository checkpointAttempts;

    public CourseQueryService(
            ThemeRepository themes,
            UserLessonProgressRepository progress,
            CheckpointAttemptRepository checkpointAttempts
    ) {
        this.themes = themes;
        this.progress = progress;
        this.checkpointAttempts = checkpointAttempts;
    }

    @Transactional(readOnly = true)
    public List<Theme> orderedThemes() {
        return themes.findAllByOrderBySortOrderAsc();
    }

    @Transactional(readOnly = true)
    public void requireThemeUnlocked(AppUser user, UUID themeId) {
        List<Theme> catalog = orderedThemes();
        boolean exists = catalog.stream().anyMatch(theme -> theme.getId().equals(themeId));
        if (!exists) {
            throw ApiException.notFound("Тема не найдена");
        }
        if (!unlockedThemeIds(user, catalog).contains(themeId)) {
            throw ApiException.forbidden("Тема ещё закрыта");
        }
    }

    public void requireLessonUnlocked(AppUser user, Lesson lesson) {
        requireThemeUnlocked(user, lesson.getTheme().getId());
    }

    public Set<UUID> unlockedThemeIds(AppUser user, List<Theme> catalog) {
        Set<UUID> completedLessons = completedLessonIds(user);
        Set<UUID> passedThemes = passedCheckpointThemeIds(user, catalog);
        Set<UUID> unlocked = new HashSet<>();
        Theme previous = null;
        for (Theme theme : catalog) {
            boolean open = previous == null
                    || (themeCompleted(previous, completedLessons) && passedThemes.contains(previous.getId()));
            if (open) {
                unlocked.add(theme.getId());
            }
            previous = theme;
        }
        return unlocked;
    }

    @Transactional(readOnly = true)
    public boolean allLessonsCompleted(AppUser user, UUID themeId) {
        Theme theme = themes.findDetailedById(themeId)
                .orElseThrow(() -> ApiException.notFound("Тема не найдена"));
        return themeCompleted(theme, completedLessonIds(user));
    }

    public boolean themeCompleted(Theme theme, Set<UUID> completedLessons) {
        if (theme.getLessons().isEmpty()) {
            return false;
        }
        return theme.getLessons().stream().allMatch(lesson -> completedLessons.contains(lesson.getId()));
    }

    public Set<UUID> completedLessonIds(AppUser user) {
        return progress.findByUser_Id(user.getId()).stream()
                .filter(UserLessonProgress::isCompleted)
                .map(item -> item.getLesson().getId())
                .collect(Collectors.toSet());
    }

    public Set<UUID> passedCheckpointThemeIds(AppUser user, List<Theme> catalog) {
        List<UUID> themeIds = catalog.stream().map(Theme::getId).toList();
        if (themeIds.isEmpty()) {
            return Set.of();
        }
        return checkpointAttempts.findByUser_IdAndCheckpoint_Theme_IdIn(user.getId(), themeIds).stream()
                .filter(CheckpointAttempt::isPassed)
                .map(attempt -> attempt.getCheckpoint().getTheme().getId())
                .collect(Collectors.toSet());
    }
}
