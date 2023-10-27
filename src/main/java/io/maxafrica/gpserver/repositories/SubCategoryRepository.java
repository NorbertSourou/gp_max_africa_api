package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>, JpaSpecificationExecutor<SubCategory> {

    boolean existsByCategoryPosition(String category_position);

    void findByCategoryPosition(String category_position);

    SubCategory findByPosition(String position);

    List<SubCategory> findByCategoryId(UUID categoryId);

    List<SubCategory> findByIdIn(List<Long> ids);
}
