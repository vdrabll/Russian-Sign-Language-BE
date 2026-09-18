package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rsl.api.Model.Lesson;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @EntityGraph(attributePaths = {"gestures", "theme"})
    @Query("select l from Lesson l where l.id = :id")
    Optional<Lesson> findWithGesturesById(UUID id);
}
