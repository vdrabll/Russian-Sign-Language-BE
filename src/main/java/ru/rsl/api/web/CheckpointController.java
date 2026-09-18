package ru.rsl.api.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.rsl.api.Model.AppUser;
import ru.rsl.api.service.CheckpointService;
import ru.rsl.api.web.dto.CheckpointAttemptResponse;
import ru.rsl.api.web.dto.CheckpointResponse;
import ru.rsl.api.web.dto.SubmitCheckpointRequest;

import java.util.UUID;

@RestController
@RequestMapping("/themes/{themeId}/checkpoint")
@Tag(name = "Checkpoints")
@SecurityRequirement(name = "userId")
public class CheckpointController {

    private final CheckpointService checkpoints;

    public CheckpointController(CheckpointService checkpoints) {
        this.checkpoints = checkpoints;
    }

    @GetMapping
    @Operation(summary = "Контрольная темы (варианты — жесты, без флага правильности)")
    public CheckpointResponse get(AppUser user, @PathVariable UUID themeId) {
        return checkpoints.get(CurrentUser.require(user), themeId);
    }

    @PostMapping("/attempts")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Сдать контрольную")
    public CheckpointAttemptResponse submit(
            AppUser user,
            @PathVariable UUID themeId,
            @Valid @RequestBody SubmitCheckpointRequest request
    ) {
        return checkpoints.submit(CurrentUser.require(user), themeId, request);
    }
}
