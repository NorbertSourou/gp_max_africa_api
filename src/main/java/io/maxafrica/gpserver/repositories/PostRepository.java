package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {
    Optional<Post> findByPosition(String value);

//    @Query("SELECT p FROM Post p JOIN p.categories c WHERE c.id = :categoryId AND p.id <> :postId")
//    List<Post> getRandomPostsByCategoryAndExcludingPostId(Long subCategoryId, UUID postId, Pageable pageable);

    @Query("SELECT p.id FROM Post p JOIN p.subCategories c WHERE c.id = :subCategories_id AND p.id <> :postId")
    List<UUID> findPostIdsExcludingPostId(UUID postId,Long subCategories_id);

    List<Post> findByIdIn(Collection<UUID> id);

}
