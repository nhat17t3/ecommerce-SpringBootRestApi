package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.CategoryArticleRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Article;
import com.nhat.demoSpringbooRestApi.models.CategoryArticle;
import com.nhat.demoSpringbooRestApi.repositories.ArticleRepo;
import com.nhat.demoSpringbooRestApi.repositories.CategoryArticleRepo;
import com.nhat.demoSpringbooRestApi.services.CategoryArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryArticleServiceImpl implements CategoryArticleService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CategoryArticleRepo categoryArticleRepo;
    @Autowired
    private ArticleRepo articleRepo;

    @Override
    public List<CategoryArticle> getAllCategoryArticles() {
        return categoryArticleRepo.findAll();
    }

    @Override
    public CategoryArticle findCategoryArticleById(int categoryArticleId) {
        return categoryArticleRepo.findById(categoryArticleId)
                .orElseThrow(() -> new ResourceNotFoundException("CategoryArticle not found with id: " + categoryArticleId));
    }


    @Override
    public CategoryArticle createCategoryArticle(CategoryArticleRequestDTO categoryArticleRequestDTO) {
        CategoryArticle categoryArticle = modelMapper.map(categoryArticleRequestDTO, CategoryArticle.class);
        return categoryArticleRepo.save(categoryArticle);
    }

    @Override
    public CategoryArticle updateCategoryArticle(int categoryArticleId, CategoryArticleRequestDTO categoryArticleRequestDTO) {
        CategoryArticle existingCategoryArticle = findCategoryArticleById(categoryArticleId);
        existingCategoryArticle.setName(categoryArticleRequestDTO.getName());
        return categoryArticleRepo.save(existingCategoryArticle);
    }

    @Override
    public boolean checkBeforeDeleteCategoryArticle(int categoryArticleId) {
        List<Article> articles = articleRepo.checkBeforeDeleteCategoryArticle(categoryArticleId);
        return articles.isEmpty();
    }

    @Override
    public void deleteCategoryArticle(int categoryArticleId) {
        CategoryArticle existingCategoryArticle = findCategoryArticleById(categoryArticleId);
        categoryArticleRepo.delete(existingCategoryArticle);
    }
}
