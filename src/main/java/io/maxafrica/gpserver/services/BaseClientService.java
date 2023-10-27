package io.maxafrica.gpserver.services;

import io.maxafrica.gpserver.dto.CategoryDTO;
import io.maxafrica.gpserver.dto.PostDTO;
import io.maxafrica.gpserver.dto.SubCategoryDTO;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BaseClientService {

//    TODO: tendances actuelles  CLIENT ( limit 3)
    List<CategoryDTO> getCategories(int limit);
    Page<CategoryDTO> getCategoriesPage(String search, int page, int size);

    SubCategoryDTO getSubCategory(Long subCategoryId);

    PostDTO getPost(UUID postId);

    // liste des sous categories (limit 4 et liste paginée)
    List<SubCategoryDTO> getSubCategoriesByCategory(UUID categoryId, String search, int limit);

    Page<SubCategoryDTO> getSubCategoriesByCategoryPage(UUID categoryId, String search, int page, int size);

    Page<PostDTO> getPostsBySubCategoryPage(Long subCategoryId, String search, int page, int size);

    Page<PostDTO> getPostsByCategoryAndSubCategoryPage(UUID categoryId, Long subCategoryId, String search, int page, int size);
//    TODO: autres videos  à suivre CLIENT


}
