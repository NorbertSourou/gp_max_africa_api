package io.maxafrica.gpserver.dto;

import io.maxafrica.gpserver.entities.Post;

import java.util.List;
import java.util.UUID;

public class CreatePostData {
    private UUID categoryId;
    private List<Long> tagIds;

    private Post post;

    public CreatePostData() {
    }

    public CreatePostData(UUID categoryId, List<Long> tagIds, Post post) {
        this.categoryId = categoryId;
        this.tagIds = tagIds;
        this.post = post;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
