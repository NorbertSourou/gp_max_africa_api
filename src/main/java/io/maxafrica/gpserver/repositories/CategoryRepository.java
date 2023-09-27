package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
