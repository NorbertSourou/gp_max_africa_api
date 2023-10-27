package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    Category findByPosition(String position);


    List<Category> findByIdIn(List<UUID> uuids);

    @Query(value = "SELECT c FROM Category c ORDER BY c.position ASC limit :value")
    List<Category> findAllLimit(int value);
}
