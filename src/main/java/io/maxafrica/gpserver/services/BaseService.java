package io.maxafrica.gpserver.services;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.CreatePostData;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.entities.Tag;


import java.util.List;
import java.util.UUID;

public interface BaseService {
    Category addCategory(Category category);

    List<Category> getCategories();

    Category updateCategory(UUID categoryId, Category category);

    ApiResponse deleteCategory(UUID categoryId);


    List<SubCategory> getSubCategory(UUID categoryId);

    SubCategory addSubCategory(UUID categoryId, SubCategory subCategory);

    SubCategory updateSubCategory(UUID categoryId, Long subCategoryId, SubCategory subCategory);

    ApiResponse deleteSubCategory(UUID categoryId, Long subCategoryId);


    //    todo : Manage post here

    List<Tag> getTags();

    Tag addTag(Tag tag);

    Tag updateTag(Long tagId, Tag tag);

    ApiResponse deleteTag(Long tagId);

    Post addPost(CreatePostData createPostData);

    Post updatePost(UUID postId, CreatePostData createPostData);
}
