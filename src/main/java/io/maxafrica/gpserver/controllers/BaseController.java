package io.maxafrica.gpserver.controllers;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.services.BaseService;
import org.springframework.web.bind.annotation.*;

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
    public List<SubCategory> getSubCategories(@PathVariable UUID categoryId) {
        return baseService.getSubCategory(categoryId);
    }

    @PostMapping("categories/{categoryId}/sub-categories")
    public SubCategory addSubCategory(@PathVariable UUID categoryId, @RequestBody SubCategory subCategory) {
        return baseService.addSubCategory(categoryId, subCategory);
    }

}
