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
@Table(name = "checkpoint_attempts")
public class CheckpointAttempt {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checkpoint_id", nullable = false)
    private Checkpoint checkpoint;

    @Column(nullable = false)
    private boolean passed;

    @Column(name = "score_percent", nullable = false)
    private int scorePercent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private List<CheckpointAnswerSnapshot> answers = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected CheckpointAttempt() {
    }

    public CheckpointAttempt(
            AppUser user,
            Checkpoint checkpoint,
            boolean passed,
            int scorePercent,
            List<CheckpointAnswerSnapshot> answers
    ) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.checkpoint = checkpoint;
        this.passed = passed;
        this.scorePercent = scorePercent;
        this.answers = answers == null ? List.of() : List.copyOf(answers);
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

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public boolean isPassed() {
        return passed;
    }

    public int getScorePercent() {
        return scorePercent;
    }

    public List<CheckpointAnswerSnapshot> getAnswers() {
        return answers;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
