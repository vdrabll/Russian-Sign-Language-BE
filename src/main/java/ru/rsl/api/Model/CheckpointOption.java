package ru.rsl.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "checkpoint_options")
public class CheckpointOption {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private CheckpointQuestion question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gesture_id", nullable = false)
    private Gesture gesture;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    protected CheckpointOption() {
    }

    public UUID getId() {
        return id;
    }

    public CheckpointQuestion getQuestion() {
        return question;
    }

    public Gesture getGesture() {
        return gesture;
    }

    public boolean isCorrect() {
        return correct;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
