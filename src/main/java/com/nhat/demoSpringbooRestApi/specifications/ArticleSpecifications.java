package com.nhat.demoSpringbooRestApi.specifications;

import com.nhat.demoSpringbooRestApi.dtos.ArticleFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Article;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ArticleSpecifications {
    public static Specification<Article> searchByCondition(ArticleFilterRequestDTO filterInput) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                // Filter by name
                String inputSearch = filterInput.getInputSearch();
                if (inputSearch != null && !inputSearch.equals("")) {
                    predicates.add(criteriaBuilder.like(root.<String>get("name"), "%" + inputSearch.trim() + "%"));
                }

                // Filter by category IDs
                List<String> categoryIds = filterInput.getCategoryArticleIds();
                if (categoryIds != null && !categoryIds.isEmpty()) {
                    predicates.add(root.get("categoryArticle").get("id").in(categoryIds));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

}
