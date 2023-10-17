package io.maxafrica.gpserver.controllers;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.services.BaseClientService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bases-clients")
public class BaseClientController {

    private final BaseClientService baseClientService;

    public BaseClientController(BaseClientService baseClientService) {
        this.baseClientService = baseClientService;
    }

    @GetMapping("/{postId}")
    public Post getPost(@PathVariable UUID postId) {
        return baseClientService.getPost(postId);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return baseClientService.getCategories();
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public SubCategory getCategories(@PathVariable Long subCategoryId) {
        return baseClientService.getSubCategory(subCategoryId);
    }

    @GetMapping("/category/{categoryId}/subcategories")
    public Page<SubCategory> getSubcategoryByCategory(@PathVariable UUID categoryId, @RequestParam(required = false, defaultValue = "") String search, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "50") int size) {
        return baseClientService.getSubCategoriesByCategoryPage(categoryId, search, page, size);
    }

    @GetMapping("/subcategory/{subcategoryId}/posts")
    public Page<Post> getPostsBySubCategoryPage(@PathVariable Long subcategoryId, @RequestParam(required = false, defaultValue = "") String search, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "50") int size) {
        return baseClientService.getPostsBySubCategoryPage(subcategoryId, search, page, size);
    }
}
