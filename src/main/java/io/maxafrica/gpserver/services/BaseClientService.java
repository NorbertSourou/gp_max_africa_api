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

    //    liste des sous categories (limit 4 et liste paginée)
    List<SubCategory> getSubCategoriesList(UUID categoryId,String search);
    Page<SubCategory> getSubCategories(UUID categoryId,String search ,int page, int size);

    Page<Post> getPosts(UUID categoryId,UUID subCategoryId, String search  ,int page, int size);


//    TODO: autres videos  à suivre CLIENT


}
