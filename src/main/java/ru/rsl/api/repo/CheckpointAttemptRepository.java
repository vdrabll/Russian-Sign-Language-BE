package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsl.api.Model.CheckpointAttempt;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CheckpointAttemptRepository extends JpaRepository<CheckpointAttempt, UUID> {

    boolean existsByUser_IdAndCheckpoint_IdAndPassedIsTrue(UUID userId, UUID checkpointId);

    List<CheckpointAttempt> findByUser_IdAndCheckpoint_Theme_IdIn(UUID userId, Collection<UUID> themeIds);
}
