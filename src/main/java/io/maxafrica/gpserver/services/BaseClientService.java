package io.maxafrica.gpserver.services;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BaseClientService {

//    TODO: tendances actuelles  CLIENT ( limit 3)
    List<Category> getCategories();

    SubCategory getSubCategory(Long subCategoryId);

    Post getPost(UUID postId);

    // liste des sous categories (limit 4 et liste paginée)
    Page<SubCategory> getSubCategoriesByCategoryPage(UUID categoryId, String search, int page, int size);

    Page<Post> getPostsBySubCategoryPage(Long subCategoryId, String search, int page, int size);

    Page<Post> getPostsByCategoryAndSubCategoryPage(UUID categoryId, Long subCategoryId, String search, int page, int size);


//    TODO: autres videos  à suivre CLIENT


}
