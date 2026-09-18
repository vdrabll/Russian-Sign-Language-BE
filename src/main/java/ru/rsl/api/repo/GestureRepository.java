package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsl.api.Model.Gesture;

import java.util.List;
import java.util.UUID;

public interface GestureRepository extends JpaRepository<Gesture, UUID> {

    List<Gesture> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}
