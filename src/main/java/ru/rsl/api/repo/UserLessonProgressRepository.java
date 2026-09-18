package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsl.api.Model.UserLessonProgress;
import ru.rsl.api.Model.UserLessonProgressId;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, UserLessonProgressId> {

    List<UserLessonProgress> findByUser_IdAndLesson_IdIn(UUID userId, Collection<UUID> lessonIds);

    List<UserLessonProgress> findByUser_Id(UUID userId);
}
