package io.maxafrica.gpserver.services.impl;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.CreatePostData;
import io.maxafrica.gpserver.dto.PostDTO;
import io.maxafrica.gpserver.dto.SubCategoryDTO;
import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.entities.Tag;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.repositories.CategoryRepository;
import io.maxafrica.gpserver.repositories.PostRepository;
import io.maxafrica.gpserver.repositories.SubCategoryRepository;
import io.maxafrica.gpserver.repositories.TagRepository;
import io.maxafrica.gpserver.services.BaseService;
import io.maxafrica.gpserver.services.specifications.PostSpecifications;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;


    @Override
    public Category addCategory(Category category) {
        category.setSubCategories(new ArrayList<>());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(UUID categoryId, Category category) {
        Category categoryDb = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        categoryDb.setName(category.getName());
        categoryDb.setPosition(category.getPosition());
        return categoryRepository.save(categoryDb);
    }

    @Override
    public ApiResponse deleteCategory(UUID categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        categoryRepository.deleteById(categoryId);
        return new ApiResponse(true, "Done");
    }

    @Override
    public List<SubCategoryDTO> getSubCategories(UUID categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId)
                .stream()
                .map(subCategory -> modelMapper.map(subCategory, SubCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubCategoryDTO getSubCategory(Long subCategoryId) {
        SubCategory subCategoryDb = subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory", "ID", subCategoryId));
        return modelMapper.map(subCategoryDb, SubCategoryDTO.class);
    }

    @Override
    public SubCategoryDTO addSubCategory(UUID categoryId, SubCategoryDTO subCategoryDTO) {
        Category categoryDb = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        SubCategory subCategory = modelMapper.map(subCategoryDTO, SubCategory.class);
        subCategory.setCategory(categoryDb);
        subCategory = subCategoryRepository.save(subCategory);
        return modelMapper.map(subCategory, SubCategoryDTO.class);
    }

    @Override
    public SubCategoryDTO updateSubCategory(Long subCategoryId, SubCategoryDTO subCategoryDTO) {
        SubCategory subCategoryDB = subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory", "ID", subCategoryId));
        subCategoryDB.setName(subCategoryDTO.getName());
        subCategoryDB.setPosition(subCategoryDTO.getPosition());
        subCategoryDB = subCategoryRepository.save(subCategoryDB);
        return modelMapper.map(subCategoryDB, SubCategoryDTO.class);
    }

    @Override
    public ApiResponse deleteSubCategory(Long subCategoryId) {
        subCategoryRepository.findById(subCategoryId).orElseThrow(() -> new ResourceNotFoundException("SubCategory", "ID", subCategoryId));
        tagRepository.deleteById(subCategoryId);
        return new ApiResponse(true, "Done");
    }


    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long tagId, Tag tag) {
        Tag tagDB = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", "ID", tagId));
        tagDB.setName(tag.getName());
        return tagRepository.save(tagDB);
    }

    @Override
    public ApiResponse deleteTag(Long tagId) {
        tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag", "ID", tagId));
        tagRepository.deleteById(tagId);
        return new ApiResponse(true, "Done");
    }

    @Override
    public PostDTO addPost(CreatePostData createPostData) {
        List<SubCategory> subCategories = subCategoryRepository.findByIdIn(createPostData.getSubCategoriesIds());
        List<Category> categories = categoryRepository.findByIdIn(createPostData.getCategoriesIds());
        List<Tag> tags = tagRepository.findByIdIn(createPostData.getTagIds());

        Post post = modelMapper.map(createPostData.getPostDto(), Post.class);
        post.setTags(tags);
        post.setCategories(new HashSet<>(categories));
        post.setSubCategories(new HashSet<>(subCategories));
        post.setTags(tags);

        // TODO: 10/26/2023 Set CREATED USER HERE

        post = postRepository.save(post);

        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(UUID postId, CreatePostData createPostData) {
        Post postDB = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        List<SubCategory> subCategories = subCategoryRepository.findByIdIn(createPostData.getSubCategoriesIds());
        List<Category> categories = categoryRepository.findByIdIn(createPostData.getCategoriesIds());
        List<Tag> tags = tagRepository.findByIdIn(createPostData.getTagIds());

        postDB = this.refactorPost(postDB, createPostData.getPostDto());
        postDB.setCategories(new HashSet<>(categories));
        postDB.setSubCategories(new HashSet<>(subCategories));
        postDB.setTags(tags);
        postDB = postRepository.save(postDB);

        return modelMapper.map(postDB, PostDTO.class);
    }

    @Override
    public Page<PostDTO> getPosts(List<UUID> categoryIds, List<Long> subCategoryIds, String search, int page, int size) {
        Specification<Post> postSpecification = PostSpecifications.filterPosts(categoryIds, subCategoryIds, search);
        Page<Post> posts = postRepository.findAll(postSpecification, PageRequest.of(page, size, Sort.by(Sort.Order.asc("title"))));
        return posts.map(post -> modelMapper.map(post, PostDTO.class));
    }

    @Override
    public List<PostDTO> getPosts(Long subCategoryId) {
        return postRepository.findAll(PostSpecifications.filterPostsBySubCategory(subCategoryId))
                .stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostById(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updateSimplePostInformation(UUID postId, PostDTO postDto) {
        Post postDB = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        postDB = postRepository.save(this.refactorPost(postDB, postDto));
        return modelMapper.map(postDB, PostDTO.class);
    }


    private Post refactorPost(Post postDB, PostDTO postDto) {
        postDB.setTags(postDto.getTags() != null ? postDto.getTags() : postDB.getTags());
        postDB.setTitle(postDto.getTitle() != null ? postDto.getTitle() : postDB.getTitle());
        postDB.setUrl(postDto.getUrl() != null ? postDto.getUrl() : postDB.getUrl());
        postDB.setImageUrl(postDto.getImageUrl() != null ? postDto.getImageUrl() : postDB.getImageUrl());
        postDB.setDescription(postDto.getDescription() != null ? postDto.getDescription() : postDB.getDescription());
        postDB.setPosition(postDto.getPosition() != null ? postDto.getPosition() : postDB.getPosition());
        postDB.setNbLikes(postDto.getNbLikes() != null ? postDto.getNbLikes() : postDB.getNbLikes());
        postDB.setNbComments(postDto.getNbComments() != null ? postDto.getNbComments() : postDB.getNbComments());
        postDB.setNbViews(postDto.getNbViews() != null ? postDto.getNbViews() : postDB.getNbViews());
        postDB.setPostLevel(postDto.getPostLevel() != null ? postDto.getPostLevel() : postDB.getPostLevel());
        return postDB;
    }

    @Override
    public ApiResponse deletePost(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "Id", postId);
        }
        postRepository.deleteById(postId);
        return new ApiResponse(true, "Done");
    }
}
