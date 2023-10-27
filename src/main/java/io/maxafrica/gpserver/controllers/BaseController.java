package io.maxafrica.gpserver.controllers;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.CreatePostData;
import io.maxafrica.gpserver.dto.PostDTO;
import io.maxafrica.gpserver.dto.SubCategoryDTO;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.entities.Tag;
import io.maxafrica.gpserver.services.BaseService;
import io.maxafrica.gpserver.utils.CustomsErrorMessage;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bases")
public class BaseController {

    private final BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping("categories")
    public List<Category> getCategories() {
        return baseService.getCategories();
    }

    @PostMapping("categories")
    public Category addCategory(@RequestBody Category category) {
        return baseService.addCategory(category);
    }

    @PutMapping("categories/{categoryId}")
    public Category updateCategory(@PathVariable UUID categoryId, @RequestBody Category category) {
        return baseService.updateCategory(categoryId, category);
    }

    @DeleteMapping("categories/{categoryId}")
    public ApiResponse deleteCategory(@PathVariable UUID categoryId) {
        return baseService.deleteCategory(categoryId);
    }

    @GetMapping("categories/{categoryId}/sub-categories")
    public List<SubCategoryDTO> getSubCategories(@PathVariable UUID categoryId) {
        return baseService.getSubCategories(categoryId);
    }

    @GetMapping("categories/{categoryId}/sub-categories/{subCategoryId}")
    public SubCategoryDTO getSubCategory(@PathVariable UUID categoryId, @PathVariable Long subCategoryId) {
        return baseService.getSubCategory(subCategoryId);
    }

    @PostMapping("categories/{categoryId}/sub-categories")
    public ResponseEntity<?> addSubCategory(@PathVariable UUID categoryId, @Valid @RequestBody SubCategoryDTO subCategoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            return CustomsErrorMessage.getValidatorMessage(result);
        }
        return ResponseEntity.ok(baseService.addSubCategory(categoryId, subCategoryDTO));
    }

    @PutMapping("categories/{categoryId}/sub-categories/{subCategoryId}")
    public SubCategoryDTO updateSubCategory(@PathVariable UUID categoryId, @PathVariable Long subCategoryId, @RequestBody SubCategoryDTO subCategoryDTO) {
        return baseService.updateSubCategory(subCategoryId, subCategoryDTO);
    }

    @DeleteMapping("categories/{categoryId}/sub-categories/{subCategoryId}")
    public ApiResponse deleteSubCategory(@PathVariable UUID categoryId, @PathVariable Long subCategoryId) {
        return baseService.deleteSubCategory(subCategoryId);
    }


    @GetMapping("tags")
    public List<Tag> getTags() {
        return baseService.getTags();
    }

    @PostMapping("tags")
    public ResponseEntity<?> addTag(@Valid @RequestBody Tag tag, BindingResult result) {
        if (result.hasErrors()) {
            return CustomsErrorMessage.getValidatorMessage(result);
        }
        return ResponseEntity.ok(baseService.addTag(tag));
    }

    @PutMapping("tags/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable Long tagId, @Valid @RequestBody Tag tag, BindingResult result) {
        if (result.hasErrors()) {
            return CustomsErrorMessage.getValidatorMessage(result);
        }
        return ResponseEntity.ok(baseService.updateTag(tagId, tag));
    }

    @DeleteMapping("tags/{tagId}")
    public ApiResponse deleteTag(@PathVariable Long tagId) {
        return baseService.deleteTag(tagId);
    }


    @PostMapping("posts")
    public ResponseEntity<?> addPost(@Valid @RequestBody CreatePostData createPostData, BindingResult result) {
        if (result.hasErrors()) {
            return CustomsErrorMessage.getValidatorMessage(result);
        }
        return ResponseEntity.ok(baseService.addPost(createPostData));
    }


    @GetMapping("posts")
    @Cacheable(value = "posts")
    public Page<PostDTO> getPots(@RequestParam(name = "categoryIds", defaultValue = "", required = false) List<UUID> categoryIds,
                                 @RequestParam(required = false, defaultValue = "") List<Long> subCategoryIds,
                                 @RequestParam(defaultValue = "", required = false) String search,
                                 @RequestParam(defaultValue = "0", required = false) int page,
                                 @RequestParam(defaultValue = "20", required = false) int size) {
        if (categoryIds.isEmpty()) {
            categoryIds = new ArrayList<>();
        }

        if (subCategoryIds.isEmpty()) {
            subCategoryIds = new ArrayList<>();
        }
        return baseService.getPosts(categoryIds, subCategoryIds, search, page, size);
    }

    @GetMapping("posts/sub-category-post")
    public List<PostDTO> getPostsBySubCategory(@RequestParam Long subCategoryId) {
        return baseService.getPosts(subCategoryId);
    }

    @PutMapping("posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable UUID postId, @Valid @RequestBody CreatePostData createPostData, BindingResult result) {
        if (result.hasErrors()) {
            return CustomsErrorMessage.getValidatorMessage(result);
        }
        return ResponseEntity.ok(baseService.updatePost(postId, createPostData));
    }

    @PutMapping("posts/{postId}/simple-update")
    public ResponseEntity<?> updateSimplePostInformation(@PathVariable UUID postId, @RequestBody PostDTO postDto, BindingResult result) {
        if (result.hasErrors()) {
            return CustomsErrorMessage.getValidatorMessage(result);
        }
        return ResponseEntity.ok(baseService.updateSimplePostInformation(postId, postDto));
    }

    @GetMapping("posts/{postId}")
    public PostDTO getPostById(@PathVariable UUID postId) {
        return baseService.getPostById(postId);
    }

    @DeleteMapping("posts/{postId}")
    public ApiResponse deletePost(@PathVariable UUID postId) {
        return baseService.deletePost(postId);
    }

}
