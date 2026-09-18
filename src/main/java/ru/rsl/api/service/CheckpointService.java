package ru.rsl.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.Model.Checkpoint;
import ru.rsl.api.Model.CheckpointAnswerSnapshot;
import ru.rsl.api.Model.CheckpointAttempt;
import ru.rsl.api.Model.CheckpointOption;
import ru.rsl.api.Model.CheckpointQuestion;
import ru.rsl.api.repo.CheckpointAttemptRepository;
import ru.rsl.api.repo.CheckpointRepository;
import ru.rsl.api.web.ApiException;
import ru.rsl.api.web.dto.CheckpointAttemptResponse;
import ru.rsl.api.web.dto.CheckpointOptionResponse;
import ru.rsl.api.web.dto.CheckpointQuestionResponse;
import ru.rsl.api.web.dto.CheckpointResponse;
import ru.rsl.api.web.dto.SubmitCheckpointRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CheckpointService {

    private final CheckpointRepository checkpoints;
    private final CheckpointAttemptRepository attempts;
    private final CourseQueryService course;

    public CheckpointService(
            CheckpointRepository checkpoints,
            CheckpointAttemptRepository attempts,
            CourseQueryService course
    ) {
        this.checkpoints = checkpoints;
        this.attempts = attempts;
        this.course = course;
    }

    @Transactional(readOnly = true)
    public CheckpointResponse get(AppUser user, UUID themeId) {
        course.requireThemeUnlocked(user, themeId);
        Checkpoint checkpoint = load(themeId);
        boolean passed = attempts.existsByUser_IdAndCheckpoint_IdAndPassedIsTrue(user.getId(), checkpoint.getId());
        return toResponse(checkpoint, passed);
    }

    @Transactional
    public CheckpointAttemptResponse submit(AppUser user, UUID themeId, SubmitCheckpointRequest request) {
        course.requireThemeUnlocked(user, themeId);
        if (!course.allLessonsCompleted(user, themeId)) {
            throw ApiException.forbidden("Сначала закрой все уроки темы");
        }
        Checkpoint checkpoint = load(themeId);
        Grade grade = grade(checkpoint, request.answers());
        var attempt = new CheckpointAttempt(
                user,
                checkpoint,
                grade.passed(),
                grade.percent(),
                request.answers()
        );
        return CheckpointAttemptResponse.from(attempts.save(attempt));
    }

    public Grade grade(Checkpoint checkpoint, List<CheckpointAnswerSnapshot> answers) {
        Map<UUID, CheckpointQuestion> questions = checkpoint.getQuestions().stream()
                .collect(Collectors.toMap(CheckpointQuestion::getId, Function.identity()));
        if (answers.size() != questions.size()) {
            throw ApiException.badRequest("Нужно ответить на все вопросы контрольной");
        }
        int correct = 0;
        for (CheckpointAnswerSnapshot answer : answers) {
            CheckpointQuestion question = questions.get(answer.questionId());
            if (question == null) {
                throw ApiException.badRequest("Неизвестный вопрос: " + answer.questionId());
            }
            CheckpointOption option = question.getOptions().stream()
                    .filter(item -> item.getId().equals(answer.selectedOptionId()))
                    .findFirst()
                    .orElseThrow(() -> ApiException.badRequest("Вариант не относится к вопросу"));
            if (option.isCorrect()) {
                correct++;
            }
        }
        int percent = Math.round(correct * 100f / questions.size());
        boolean passed = percent >= checkpoint.getTheme().getCheckpointPassPercent();
        return new Grade(passed, percent);
    }

    private Checkpoint load(UUID themeId) {
        return checkpoints.findDetailedByThemeId(themeId)
                .orElseThrow(() -> ApiException.notFound("Контрольная не найдена"));
    }

    private CheckpointResponse toResponse(Checkpoint checkpoint, boolean passed) {
        List<CheckpointQuestionResponse> questions = new ArrayList<>();
        for (CheckpointQuestion question : checkpoint.getQuestions()) {
            questions.add(new CheckpointQuestionResponse(
                    question.getId(),
                    question.getPrompt(),
                    question.getSortOrder(),
                    question.getOptions().stream().map(CheckpointOptionResponse::from).toList()
            ));
        }
        return new CheckpointResponse(
                checkpoint.getId(),
                checkpoint.getTheme().getId(),
                checkpoint.getTitle(),
                checkpoint.getTheme().getCheckpointPassPercent(),
                passed,
                questions
        );
    }

    public record Grade(boolean passed, int percent) {
    }
}
