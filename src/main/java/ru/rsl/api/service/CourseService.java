package ru.rsl.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsl.api.Model.UserLessonProgressId;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.Model.Gesture;
import ru.rsl.api.Model.Lesson;
import ru.rsl.api.Model.PracticeAttempt;
import ru.rsl.api.Model.Theme;
import ru.rsl.api.Model.UserLessonProgress;
import ru.rsl.api.repo.GestureRepository;
import ru.rsl.api.repo.LessonRepository;
import ru.rsl.api.repo.PracticeAttemptRepository;
import ru.rsl.api.repo.ThemeRepository;
import ru.rsl.api.repo.UserLessonProgressRepository;
import ru.rsl.api.web.ApiException;
import ru.rsl.api.web.dto.GestureProgressResponse;
import ru.rsl.api.web.dto.GestureResponse;
import ru.rsl.api.web.dto.LessonDetailResponse;
import ru.rsl.api.web.dto.LessonSummaryResponse;
import ru.rsl.api.web.dto.PracticeRequest;
import ru.rsl.api.web.dto.PracticeResponse;
import ru.rsl.api.web.dto.ProgressResponse;
import ru.rsl.api.web.dto.ThemeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseQueryService course;
    private final ThemeRepository themes;
    private final LessonRepository lessons;
    private final GestureRepository gestures;
    private final UserLessonProgressRepository progress;
    private final PracticeAttemptRepository attempts;
    private final MockSignAnalyzer analyzer;

    public CourseService(
            CourseQueryService course,
            ThemeRepository themes,
            LessonRepository lessons,
            GestureRepository gestures,
            UserLessonProgressRepository progress,
            PracticeAttemptRepository attempts,
            MockSignAnalyzer analyzer
    ) {
        this.course = course;
        this.themes = themes;
        this.lessons = lessons;
        this.gestures = gestures;
        this.progress = progress;
        this.attempts = attempts;
        this.analyzer = analyzer;
    }

    @Transactional(readOnly = true)
    public List<ThemeResponse> listThemes(AppUser user) {
        return mapThemes(user, course.orderedThemes());
    }

    @Transactional(readOnly = true)
    public ThemeResponse getTheme(AppUser user, UUID themeId) {
        Theme theme = themes.findDetailedById(themeId)
                .orElseThrow(() -> ApiException.notFound("Тема не найдена"));
        return mapThemes(user, List.of(theme)).getFirst();
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse getLesson(AppUser user, UUID lessonId) {
        Lesson lesson = loadLesson(lessonId);
        course.requireLessonUnlocked(user, lesson);
        UserLessonProgress state = findProgress(user, lesson);
        boolean videoWatched = state != null && state.getVideoWatchedAt() != null;
        boolean completed = state != null && state.isCompleted();
        List<GestureProgressResponse> gestureProgress = lesson.getGestures().stream()
                .map(gesture -> new GestureProgressResponse(
                        gesture.getId(),
                        gesture.getName(),
                        gesture.getDescription(),
                        gesture.getVideoUrl(),
                        attempts.existsByUser_IdAndLesson_IdAndGesture_IdAndSuccessIsTrue(
                                user.getId(), lesson.getId(), gesture.getId()
                        )
                ))
                .toList();
        return LessonDetailResponse.from(lesson, videoWatched, completed, gestureProgress);
    }

    @Transactional
    public LessonDetailResponse watch(AppUser user, UUID lessonId) {
        Lesson lesson = loadLesson(lessonId);
        course.requireLessonUnlocked(user, lesson);
        UserLessonProgress state = getOrCreateProgress(user, lesson);
        state.markVideoWatched();
        maybeComplete(user, lesson, state);
        return getLesson(user, lessonId);
    }

    @Transactional
    public PracticeResponse practice(AppUser user, UUID lessonId, PracticeRequest request) {
        Lesson lesson = loadLesson(lessonId);
        course.requireLessonUnlocked(user, lesson);
        Gesture gesture = lesson.getGestures().stream()
                .filter(item -> item.getId().equals(request.gestureId()))
                .findFirst()
                .orElseThrow(() -> ApiException.badRequest("Этот жест не входит в урок"));
        SignAnalysis analysis = analyzer.analyze(request.recordingUrl());
        var attempt = attempts.save(new PracticeAttempt(
                user,
                lesson,
                gesture,
                request.recordingUrl(),
                analysis.success(),
                analysis.errors()
        ));
        UserLessonProgress state = getOrCreateProgress(user, lesson);
        maybeComplete(user, lesson, state);
        return PracticeResponse.from(attempt, state.isCompleted());
    }

    @Transactional(readOnly = true)
    public List<GestureResponse> dictionary(String query) {
        if (query == null || query.isBlank()) {
            return gestures.findAll().stream().map(GestureResponse::from).toList();
        }
        return gestures.findByNameContainingIgnoreCaseOrderByNameAsc(query.trim()).stream()
                .map(GestureResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GestureResponse dictionaryItem(UUID gestureId) {
        return GestureResponse.from(gestures.findById(gestureId)
                .orElseThrow(() -> ApiException.notFound("Жест не найден")));
    }

    @Transactional(readOnly = true)
    public ProgressResponse progress(AppUser user) {
        List<ThemeResponse> themeResponses = listThemes(user);
        int unlocked = (int) themeResponses.stream().filter(ThemeResponse::unlocked).count();
        int completedThemes = (int) themeResponses.stream().filter(ThemeResponse::completed).count();
        int completedLessons = (int) themeResponses.stream()
                .flatMap(theme -> theme.lessons().stream())
                .filter(LessonSummaryResponse::completed)
                .count();
        return new ProgressResponse(unlocked, completedThemes, completedLessons, themeResponses);
    }

    private List<ThemeResponse> mapThemes(AppUser user, List<Theme> catalog) {
        List<Theme> all = course.orderedThemes();
        Set<UUID> unlocked = course.unlockedThemeIds(user, all);
        Set<UUID> completedLessons = course.completedLessonIds(user);
        Set<UUID> passedCheckpoints = course.passedCheckpointThemeIds(user, all);
        List<ThemeResponse> result = new ArrayList<>();
        for (Theme theme : catalog) {
            boolean themeUnlocked = unlocked.contains(theme.getId());
            boolean lessonsDone = course.themeCompleted(theme, completedLessons);
            boolean checkpointPassed = passedCheckpoints.contains(theme.getId());
            List<LessonSummaryResponse> lessonResponses = theme.getLessons().stream()
                    .map(lesson -> LessonSummaryResponse.from(lesson, completedLessons.contains(lesson.getId())))
                    .toList();
            result.add(new ThemeResponse(
                    theme.getId(),
                    theme.getTitle(),
                    theme.getDescription(),
                    theme.getSortOrder(),
                    themeUnlocked,
                    lessonsDone && checkpointPassed,
                    checkpointPassed,
                    lessonResponses
            ));
        }
        return result;
    }

    private Lesson loadLesson(UUID lessonId) {
        return lessons.findWithGesturesById(lessonId)
                .orElseThrow(() -> ApiException.notFound("Урок не найден"));
    }

    private UserLessonProgress findProgress(AppUser user, Lesson lesson) {
        return progress.findById(new UserLessonProgressId(user.getId(), lesson.getId()))
                .orElse(null);
    }

    private UserLessonProgress getOrCreateProgress(AppUser user, Lesson lesson) {
        return progress.findById(new UserLessonProgressId(user.getId(), lesson.getId()))
                .orElseGet(() -> progress.save(new UserLessonProgress(user, lesson)));
    }

    private void maybeComplete(AppUser user, Lesson lesson, UserLessonProgress state) {
        if (state.getVideoWatchedAt() == null) {
            return;
        }
        boolean allPracticed = lesson.getGestures().stream().allMatch(gesture ->
                attempts.existsByUser_IdAndLesson_IdAndGesture_IdAndSuccessIsTrue(
                        user.getId(), lesson.getId(), gesture.getId()
                ));
        if (allPracticed) {
            state.markCompleted();
        }
    }
}
