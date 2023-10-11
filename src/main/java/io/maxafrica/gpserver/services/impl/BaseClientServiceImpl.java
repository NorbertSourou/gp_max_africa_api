package io.maxafrica.gpserver.services.impl;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import io.maxafrica.gpserver.services.BaseClientService;
import io.maxafrica.gpserver.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class BaseClientServiceImpl implements BaseClientService {

    public BaseClientServiceImpl() {
    }

    @Override
    public List<Category> getCategories() {
        return null;
    }

    @Override
    public List<SubCategory> getSubCategoriesList(UUID categoryId, String search) {
        return null;
    }

    @Override
    public Page<SubCategory> getSubCategories(UUID categoryId, String search, int page, int size) {
        return null;
    }

    @Override
    public Page<Post> getPosts(UUID categoryId, UUID subCategoryId, String search, int page, int size) {
        return null;
    }
}
