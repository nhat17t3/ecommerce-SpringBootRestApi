package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.CategoryArticleRequestDTO;
import com.nhat.demoSpringbooRestApi.models.CategoryArticle;

import java.util.List;


public interface CategoryArticleService {

    List<CategoryArticle> getAllCategoryArticles ();

    CategoryArticle findCategoryArticleById(int categoryArticleId);

    CategoryArticle createCategoryArticle (CategoryArticleRequestDTO categoryArticle);

    CategoryArticle updateCategoryArticle (int categoryArticleId, CategoryArticleRequestDTO categoryArticle);

    boolean checkBeforeDeleteCategoryArticle(int categoryArticleId);

    void deleteCategoryArticle (int categoryArticleId);

}
