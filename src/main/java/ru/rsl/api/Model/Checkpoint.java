package ru.rsl.api.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "checkpoints")
public class Checkpoint {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theme_id", nullable = false, unique = true)
    private Theme theme;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "checkpoint", cascade = CascadeType.ALL)
    @OrderBy("sortOrder ASC")
    private List<CheckpointQuestion> questions = new ArrayList<>();

    protected Checkpoint() {
    }

    public UUID getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public String getTitle() {
        return title;
    }

    public List<CheckpointQuestion> getQuestions() {
        return questions;
    }
}
