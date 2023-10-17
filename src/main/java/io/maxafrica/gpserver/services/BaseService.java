package io.maxafrica.gpserver.services;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.CreatePostData;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.entities.Tag;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.UUID;

public interface BaseService {
    //   todo : Manage categories here
    Category addCategory(Category category);

    List<Category> getCategories();

    Category updateCategory(UUID categoryId, Category category);

    ApiResponse deleteCategory(UUID categoryId);

    //    todo : Manage sub categories here
    List<SubCategory> getSubCategories(UUID categoryId);

    SubCategory getSubCategory(Long subCategoryId);

    SubCategory addSubCategory(UUID categoryId, SubCategory subCategory);

    SubCategory updateSubCategory(Long subCategoryId, SubCategory subCategory);

    ApiResponse deleteSubCategory(Long subCategoryId);


    //    todo : Manage post here
    Page<Post> getPosts(UUID categoryId, UUID subCategoryId, String search, int page, int size);

    List<Post> getPosts(Long subCategoryId);

    Post getPost(UUID postId);

    Post addPost(Long subCategoryId, Post post);

    Post updatePost(UUID postId, Post post);

    ApiResponse deletePost(UUID postId);

    //   todo : Manage tags here
    List<Tag> getTags();

    Tag addTag(Tag tag);

    Tag updateTag(Long tagId, Tag tag);

    ApiResponse deleteTag(Long tagId);

    Post addPost(CreatePostData createPostData);

    Post updatePost(UUID postId, CreatePostData createPostData);


    //    TODO: autres videos  à suivre

    //    TODO: crud des tendances actuelles ( limit 3)
}
