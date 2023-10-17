package io.maxafrica.gpserver.services.impl;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.CreatePostData;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.entities.Tag;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.repositories.CategoryRepository;
import io.maxafrica.gpserver.repositories.PostRepository;
import io.maxafrica.gpserver.repositories.SubCategoryRepository;
import io.maxafrica.gpserver.repositories.TagRepository;
import io.maxafrica.gpserver.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BaseServiceImpl implements BaseService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public BaseServiceImpl(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository,
                           TagRepository tagRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Category addCategory(Category category) {
        category.setSubCategories(new ArrayList<>());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(UUID categoryId, Category category) {
        Category categoryDb = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        categoryDb.setName(category.getName());
        categoryDb.setPosition(category.getPosition());
        return categoryRepository.save(categoryDb);
    }

    @Override
    public ApiResponse deleteCategory(UUID categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        categoryRepository.deleteById(categoryId);
        return new ApiResponse(true, "Done");
    }

    @Override
    public List<SubCategory> getSubCategories(UUID categoryId) {
        return null;
    }

    @Override
    public SubCategory getSubCategory(Long subCategoryId) {
        return null;
    }

    @Override
    public SubCategory addSubCategory(UUID categoryId, SubCategory subCategory) {
        return null;
    }

    @Override
    public SubCategory updateSubCategory(Long subCategoryId, SubCategory subCategory) {
        return null;
    }

    @Override
    public ApiResponse deleteSubCategory(Long subCategoryId) {
        return null;
    }


    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long tagId, Tag tag) {
        Tag tagDB = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", "ID", tagId));
        tagDB.setName(tag.getName());
        return tagRepository.save(tagDB);
    }

    @Override
    public ApiResponse deleteTag(Long tagId) {
        tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", "ID", tagId));
        tagRepository.deleteById(tagId);
        return new ApiResponse(true, "Done");
    }

    @Override
    public Post addPost(CreatePostData createPostData) {
        return null;
    }

    @Override
    public Post updatePost(UUID postId, CreatePostData createPostData) {
        return null;
    }

    @Override
    public Page<Post> getPosts(UUID categoryId, UUID subCategoryId, String search, int page, int size) {
        return null;
    }

    @Override
    public List<Post> getPosts(Long subCategoryId) {
        return null;
    }

    @Override
    public Post getPost(UUID postId) {
        return null;
    }

    @Override
    public Post addPost(Long subCategoryId, Post post) {
        return null;
    }

    @Override
    public Post updatePost(UUID postId, Post post) {
        return null;
    }

    @Override
    public ApiResponse deletePost(UUID postId) {
        return null;
    }
}
