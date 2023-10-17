package io.maxafrica.gpserver.services.impl;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.repositories.CategoryRepository;
import io.maxafrica.gpserver.repositories.PostRepository;
import io.maxafrica.gpserver.repositories.SubCategoryRepository;
import io.maxafrica.gpserver.services.BaseClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class BaseClientServiceImpl implements BaseClientService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PostRepository postRepository;

    public BaseClientServiceImpl(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public SubCategory getSubCategory(Long subCategoryId) {
        return subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", subCategoryId));
    }

    //post autres videos à suivre
    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    public Page<SubCategory> getSubCategoriesByCategoryPage(UUID categoryId, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));
        String key = "%" + search.toLowerCase() + "%";
        Specification<SubCategory> subCategorySpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), criteriaBuilder.literal(key));
        subCategorySpecification = subCategorySpecification.and((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        return subCategoryRepository.findAll(subCategorySpecification, pageable);
    }

    @Override
    public Page<Post> getPostsBySubCategoryPage(Long subCategoryId, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
        String key = "%" + search.toLowerCase() + "%";
        Specification<Post> postsSpecification = (root, query, criteriaBuilder)
                -> criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                criteriaBuilder.literal(key)), criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), criteriaBuilder.literal(key)));
        postsSpecification = postsSpecification.and((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("subCategory").get("id"), subCategoryId));
        return postRepository.findAll(postsSpecification, pageable);
    }

    @Override
    public Page<Post> getPostsByCategoryAndSubCategoryPage(UUID categoryId, Long subCategoryId, String search, int page, int size) {
        return null;
    }

}
