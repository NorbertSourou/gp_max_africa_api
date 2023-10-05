package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}