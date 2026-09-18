package ru.rsl.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_lesson_progress")
@IdClass(UserLessonProgressId.class)
public class UserLessonProgress {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "video_watched_at")
    private Instant videoWatchedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    protected UserLessonProgress() {
    }

    public UserLessonProgress(AppUser user, Lesson lesson) {
        this.user = user;
        this.lesson = lesson;
    }

    public AppUser getUser() {
        return user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Instant getVideoWatchedAt() {
        return videoWatchedAt;
    }

    public void markVideoWatched() {
        if (this.videoWatchedAt == null) {
            this.videoWatchedAt = Instant.now();
        }
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public boolean isCompleted() {
        return completedAt != null;
    }

    public void markCompleted() {
        if (this.completedAt == null) {
            this.completedAt = Instant.now();
        }
    }
}
