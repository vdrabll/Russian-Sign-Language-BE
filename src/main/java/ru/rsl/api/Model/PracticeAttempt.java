package ru.rsl.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "practice_attempts")
public class PracticeAttempt {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gesture_id", nullable = false)
    private Gesture gesture;

    @Column(name = "recording_url", nullable = false)
    private String recordingUrl;

    @Column(nullable = false)
    private boolean success;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<SignErrorType> errors = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected PracticeAttempt() {
    }

    public PracticeAttempt(
            AppUser user,
            Lesson lesson,
            Gesture gesture,
            String recordingUrl,
            boolean success,
            List<SignErrorType> errors
    ) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.lesson = lesson;
        this.gesture = gesture;
        this.recordingUrl = recordingUrl;
        this.success = success;
        this.errors = errors == null ? List.of() : List.copyOf(errors);
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Gesture getGesture() {
        return gesture;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<SignErrorType> getErrors() {
        return errors;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
