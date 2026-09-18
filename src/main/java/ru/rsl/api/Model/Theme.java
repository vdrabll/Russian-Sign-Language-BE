package ru.rsl.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "themes")
public class Theme {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "checkpoint_pass_percent", nullable = false)
    private int checkpointPassPercent;

    @OneToMany(mappedBy = "theme")
    @OrderBy("sortOrder ASC")
    private List<Lesson> lessons = new ArrayList<>();

    @OneToOne(mappedBy = "theme")
    private Checkpoint checkpoint;

    protected Theme() {
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public int getCheckpointPassPercent() {
        return checkpointPassPercent;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }
}
