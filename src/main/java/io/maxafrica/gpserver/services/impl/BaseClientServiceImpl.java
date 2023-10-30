package io.maxafrica.gpserver.services.impl;

import io.maxafrica.gpserver.dto.CategoryDTO;
import io.maxafrica.gpserver.dto.PostDTO;
import io.maxafrica.gpserver.dto.SubCategoryDTO;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.repositories.CategoryRepository;
import io.maxafrica.gpserver.repositories.PostRepository;
import io.maxafrica.gpserver.repositories.SubCategoryRepository;
import io.maxafrica.gpserver.services.BaseClientService;
import io.maxafrica.gpserver.services.specifications.PostSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BaseClientServiceImpl implements BaseClientService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;


    @Override
    public Category getCategory(UUID categoryUUID) {
        return categoryRepository.findById(categoryUUID).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryUUID));
    }

    @Override
    public List<CategoryDTO> getCategories(int limit) {
        return categoryRepository.findAllLimit(limit)
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> getCategoriesPage(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("position")));
        Specification<Category> categorySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + search.toLowerCase() + "%");
        return categoryRepository.findAll(categorySpecification, pageable).map(subCategory -> modelMapper.map(subCategory, CategoryDTO.class));
    }

    @Override
    public SubCategoryDTO getSubCategory(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", subCategoryId));
        return modelMapper.map(subCategory, SubCategoryDTO.class);
    }

    //post autres videos à suivre
    @Override
    public PostDTO getPost(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<SubCategoryDTO> getSubCategoriesByCategory(UUID categoryId, String search, int limit) {
        return this.getSubCategoriesByCategoryPage(categoryId, search, 0, limit).getContent();
    }

    @Override
    public Page<SubCategoryDTO> getSubCategoriesByCategoryPage(UUID categoryId, String search, int page, int size) {
        getCategory(categoryId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("position")));
        String key = "%" + search.toLowerCase() + "%";
        Specification<SubCategory> subCategorySpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), criteriaBuilder.literal(key));
        subCategorySpecification = subCategorySpecification.and((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        return subCategoryRepository.findAll(subCategorySpecification, pageable).map(subCategory -> modelMapper.map(subCategory, SubCategoryDTO.class));
    }

    @Override
    public Page<PostDTO> getPostsBySubCategoryPage(Long subCategoryId, String search, int page, int size) {
        getSubCategory(subCategoryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
        return postRepository.findAll(PostSpecifications.filterPostsBySubCategoryAndSearch(subCategoryId, search), pageable).map(post -> modelMapper.map(post, PostDTO.class));
    }

    @Override
    public Page<PostDTO> getPostsByCategoryAndSubCategoryPage(UUID categoryId, Long subCategoryId, String search, int page, int size) {
        getCategory(categoryId);
        getSubCategory(subCategoryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
        List<UUID> categoryIds = new ArrayList<>();
        List<Long> subCategoryIds = new ArrayList<>();

        if (categoryId != null) {
            categoryIds.add(categoryId);
        }

        if (subCategoryId != null) {
            subCategoryIds.add(subCategoryId);
        }
        return postRepository.findAll(PostSpecifications.filterPosts(categoryIds, subCategoryIds, search), pageable).map(post -> modelMapper.map(post, PostDTO.class));
    }

}
