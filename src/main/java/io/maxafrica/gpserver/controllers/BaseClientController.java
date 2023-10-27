package io.maxafrica.gpserver.controllers;

import io.maxafrica.gpserver.dto.CategoryDTO;
import io.maxafrica.gpserver.dto.PostDTO;
import io.maxafrica.gpserver.dto.SubCategoryDTO;
import io.maxafrica.gpserver.exceptions.RequestNotAcceptableException;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.services.BaseClientService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("posts/{postId}")
    public PostDTO getPost(@PathVariable UUID postId) {
        return baseClientService.getPost(postId);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getCategories(@RequestParam(required = false, defaultValue = "10") int limit) {
        return baseClientService.getCategories(limit);
    }

    @GetMapping("/categories/pages")
    public Page<CategoryDTO> getCategoriesPage(@RequestParam(defaultValue = "", required = false) String search,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "50") int size) {
        return baseClientService.getCategoriesPage(search, page, size);
    }



    @GetMapping("/category/{categoryId}/subcategories/page")
    public Page<SubCategoryDTO> getSubCategoriesByCategoryPage(@PathVariable UUID categoryId,
                                                         @RequestParam(required = false, defaultValue = "") String search,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "50") int size) {
        return baseClientService.getSubCategoriesByCategoryPage(categoryId, search, page, size);
    }

    @GetMapping("/category/{categoryId}/subcategories")
    public List<SubCategoryDTO> getSubCategoriesByCategory(@PathVariable UUID categoryId,
                                                         @RequestParam(required = false, defaultValue = "") String search,
                                                         @RequestParam(required = false, defaultValue = "4") int limit) {
        return baseClientService.getSubCategoriesByCategory(categoryId, search, limit);
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<?> getSubCategory(@PathVariable Long subCategoryId) {
        return ResponseEntity.ok(baseClientService.getSubCategory(subCategoryId));
    }

    @GetMapping("/subcategory/{subcategoryId}/posts")
    public Page<PostDTO> getPostsBySubCategoryPage(@PathVariable Long subcategoryId,
                                                   @RequestParam(required = false, defaultValue = "") String search,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "50") int size) {
        return baseClientService.getPostsBySubCategoryPage(subcategoryId, search, page, size);
    }

    @GetMapping("posts")
    public Page<PostDTO> getPostsByCategoryAndSubCategoryPage(@RequestParam(required = false, defaultValue = "") UUID categoryId, @RequestParam (required = false, defaultValue = "")  Long subCategoryId,
                                                   @RequestParam(required = false, defaultValue = "") String search,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "50") int size) {
        return baseClientService.getPostsByCategoryAndSubCategoryPage(categoryId, subCategoryId, search, page, size);
    }
}
