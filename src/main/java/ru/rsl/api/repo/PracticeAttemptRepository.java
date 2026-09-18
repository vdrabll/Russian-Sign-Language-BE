package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsl.api.Model.PracticeAttempt;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PracticeAttemptRepository extends JpaRepository<PracticeAttempt, UUID> {

    boolean existsByUser_IdAndLesson_IdAndGesture_IdAndSuccessIsTrue(
            UUID userId,
            UUID lessonId,
            UUID gestureId
    );

    List<PracticeAttempt> findByUser_IdAndLesson_IdAndGesture_IdIn(
            UUID userId,
            UUID lessonId,
            Collection<UUID> gestureIds
    );
}
