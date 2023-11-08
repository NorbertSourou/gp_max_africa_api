package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailOrUsername(String email, String username);



    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
