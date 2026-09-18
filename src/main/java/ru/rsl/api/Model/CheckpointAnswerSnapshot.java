package ru.rsl.api.Model;

import java.util.UUID;

public record CheckpointAnswerSnapshot(UUID questionId, UUID selectedOptionId) {
}
