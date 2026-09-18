package ru.rsl.api.web.dto;

import ru.rsl.api.Model.CheckpointAttempt;

import java.time.Instant;
import java.util.UUID;

public record CheckpointAttemptResponse(
        UUID id,
        boolean passed,
        int scorePercent,
        Instant createdAt
) {

    public static CheckpointAttemptResponse from(CheckpointAttempt attempt) {
        return new CheckpointAttemptResponse(
                attempt.getId(),
                attempt.isPassed(),
                attempt.getScorePercent(),
                attempt.getCreatedAt()
        );
    }
}
