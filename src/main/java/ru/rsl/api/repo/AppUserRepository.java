package ru.rsl.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rsl.api.Model.AppUser;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
}
