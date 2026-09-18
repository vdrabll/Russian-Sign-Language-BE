package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rsl.api.Model.Checkpoint;

import java.util.Optional;
import java.util.UUID;

public interface CheckpointRepository extends JpaRepository<Checkpoint, UUID> {

    @EntityGraph(attributePaths = {"questions", "questions.options", "questions.options.gesture", "theme"})
    @Query("select c from Checkpoint c where c.theme.id = :themeId")
    Optional<Checkpoint> findDetailedByThemeId(UUID themeId);
}
