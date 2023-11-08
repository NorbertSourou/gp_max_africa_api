package io.maxafrica.gpserver.services.specifications;

import io.maxafrica.gpserver.entities.Category;
import io.maxafrica.gpserver.entities.Post;
import io.maxafrica.gpserver.entities.SubCategory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

public class PostSpecifications {

    public static Specification<Post> filterPosts(List<UUID> categoryIds, List<Long> subCategoryIds, String search) {
        search = "%" + search + "%";
        String finalSearch = search;
        Specification<Post> postSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.or( criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), finalSearch.toLowerCase()),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), finalSearch.toLowerCase()));

        if (categoryIds.size() > 0) {
            Specification<Post> postSpecificationCategories = (root, query, criteriaBuilder) -> {
                Subquery<UUID> subquery = query.subquery(UUID.class);
                Root<Category> categoryRoot = subquery.from(Category.class);
                subquery.select(categoryRoot.get("id"));
                subquery.where(categoryRoot.get("id").in(categoryIds));
                return criteriaBuilder.in(root.get("categories").get("id")).value(subquery);
            };
            postSpecification = postSpecification.and(postSpecificationCategories);
        }


        if (subCategoryIds.size() > 0) {
            Specification<Post> postSpecificationSubCategories = (root, query, criteriaBuilder) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<SubCategory> categoryRoot = subquery.from(SubCategory.class);
                subquery.select(categoryRoot.get("id"));
                subquery.where(categoryRoot.get("id").in(subCategoryIds));
                return criteriaBuilder.in(root.get("subCategories").get("id")).value(subquery);
            };

            postSpecification = postSpecification.and(postSpecificationSubCategories);
        }

        return postSpecification;
    }

    public static Specification<Post> filterPostsBySubCategory(Long subCategoryId) {
        return (root, query, criteriaBuilder) ->{
            Join<SubCategory, Post> postSubCategoryJoin = root.join("subCategories");
            return criteriaBuilder.equal(postSubCategoryJoin.get("id"), subCategoryId);
        };
    }

    public static Specification<Post> filterPostsBySubCategoryAndSearch(Long subCategoryId, String search) {
        search = "%" + search + "%";
        Specification<Post> postSpecification =  (root, query, criteriaBuilder) ->{
            Join<SubCategory, Post> postSubCategoryJoin = root.join("subCategories");
            return criteriaBuilder.equal(postSubCategoryJoin.get("id"), subCategoryId);
        };

        String finalSearch = search;
        postSpecification = postSpecification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), finalSearch.toLowerCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), finalSearch.toLowerCase())));


        return postSpecification;
    }

}
