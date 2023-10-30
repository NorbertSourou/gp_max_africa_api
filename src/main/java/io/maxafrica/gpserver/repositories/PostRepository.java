package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {
    Optional<Post> findByPosition(String value);

    @Query("SELECT p FROM Post p JOIN p.categories c WHERE c.id = :categoryId AND p.id <> :postId ORDER BY FUNCTION('RAND')")
    List<Post> getRandomPostsByCategoryAndExcludingPostId(UUID categoryId, UUID postId, Pageable pageable);
}
