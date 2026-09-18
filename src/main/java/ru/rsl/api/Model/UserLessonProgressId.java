package ru.rsl.api.Model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserLessonProgressId implements Serializable {

    private UUID user;
    private UUID lesson;

    public UserLessonProgressId() {
    }

    public UserLessonProgressId(UUID user, UUID lesson) {
        this.user = user;
        this.lesson = lesson;
    }

    public UUID getUser() {
        return user;
    }

    public UUID getLesson() {
        return lesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLessonProgressId that)) {
            return false;
        }
        return Objects.equals(user, that.user) && Objects.equals(lesson, that.lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, lesson);
    }
}
