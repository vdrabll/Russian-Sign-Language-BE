package ru.rsl.api.web.dto;

import ru.rsl.api.Model.PracticeAttempt;
import ru.rsl.api.Model.SignErrorType;

import java.util.List;
import java.util.UUID;

public record PracticeResponse(
        UUID attemptId,
        boolean success,
        List<SignErrorType> errors,
        boolean lessonCompleted
) {

    public static PracticeResponse from(PracticeAttempt attempt, boolean lessonCompleted) {
        return new PracticeResponse(
                attempt.getId(),
                attempt.isSuccess(),
                attempt.getErrors(),
                lessonCompleted
        );
    }
}
