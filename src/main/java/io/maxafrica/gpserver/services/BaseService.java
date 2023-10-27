package io.maxafrica.gpserver.services;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.CreatePostData;
import io.maxafrica.gpserver.dto.PostDTO;
import io.maxafrica.gpserver.dto.SubCategoryDTO;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.entities.Tag;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.UUID;

public interface BaseService {
    Category addCategory(Category category);

    List<Category> getCategories();

    Category updateCategory(UUID categoryId, Category category);

    ApiResponse deleteCategory(UUID categoryId);

    List<SubCategoryDTO> getSubCategories(UUID categoryId);

    SubCategoryDTO getSubCategory(Long subCategoryId);

    SubCategoryDTO addSubCategory(UUID categoryId, SubCategoryDTO subCategoryDTO);

    SubCategoryDTO updateSubCategory(Long subCategoryId, SubCategoryDTO subCategoryDTO);

    ApiResponse deleteSubCategory(Long subCategoryId);


    Page<PostDTO> getPosts(List<UUID> categoryIds, List<Long> subCategoryIds, String search, int page, int size);

    List<PostDTO> getPosts(Long subCategoryId);

    PostDTO getPostById(UUID postId);

    PostDTO updateSimplePostInformation(UUID postId, PostDTO postDto);

    ApiResponse deletePost(UUID postId);

    List<Tag> getTags();

    Tag addTag(Tag tag);

    Tag updateTag(Long tagId, Tag tag);

    ApiResponse deleteTag(Long tagId);

    PostDTO addPost(CreatePostData createPostData);

    PostDTO updatePost(UUID postId, CreatePostData createPostData);


    //    TODO: autres videos  à suivre

    //    TODO: crud des tendances actuelles ( limit 3)
}
