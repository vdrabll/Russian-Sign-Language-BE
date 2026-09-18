package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rsl.api.Model.Theme;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ThemeRepository extends JpaRepository<Theme, UUID> {

    @EntityGraph(attributePaths = {"lessons", "checkpoint"})
    List<Theme> findAllByOrderBySortOrderAsc();

    @EntityGraph(attributePaths = {"lessons", "checkpoint"})
    @Query("select t from Theme t where t.id = :id")
    Optional<Theme> findDetailedById(UUID id);
}
