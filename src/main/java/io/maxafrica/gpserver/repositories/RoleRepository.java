package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String userRole);
}
