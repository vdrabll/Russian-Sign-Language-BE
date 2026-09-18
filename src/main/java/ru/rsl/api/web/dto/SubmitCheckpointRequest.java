package ru.rsl.api.web.dto;

import jakarta.validation.constraints.NotEmpty;
import ru.rsl.api.Model.CheckpointAnswerSnapshot;

import java.util.List;

public record SubmitCheckpointRequest(@NotEmpty List<CheckpointAnswerSnapshot> answers) {
}
