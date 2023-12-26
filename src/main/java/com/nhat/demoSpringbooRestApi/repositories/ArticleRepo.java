package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {
    Page<Article> findAll(Specification<Article> spec, Pageable pageable);
    @Query("SELECT article FROM Article article "
            + "JOIN FETCH article.categoryArticle categoryArticle "
            + "WHERE categoryArticle.id = ?1")
    List<Article> checkBeforeDeleteCategoryArticle(int categoryArticleId);
}
