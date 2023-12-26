package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.ArticleFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.ArticleRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Article;
import com.nhat.demoSpringbooRestApi.models.CategoryArticle;
import com.nhat.demoSpringbooRestApi.repositories.ArticleRepo;
import com.nhat.demoSpringbooRestApi.services.ArticleService;
import com.nhat.demoSpringbooRestApi.services.CategoryArticleService;
import com.nhat.demoSpringbooRestApi.specifications.ArticleSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    MinIOService minIOService;
    @Autowired
    private ArticleRepo articleRepo;
    @Autowired
    private CategoryArticleService categoryArticleService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DataTableResponseDTO<Article> getAllArticles(ArticleFilterRequestDTO articleFilterRequestDTO) {
        Sort sortByAndOrder = articleFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(articleFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(articleFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(articleFilterRequestDTO.getPageNumber(), articleFilterRequestDTO.getPageSize(), sortByAndOrder);
        Specification<Article> spec = ArticleSpecifications.searchByCondition(articleFilterRequestDTO);
        Page<Article> pageArticles = articleRepo.findAll(spec, pageDetails);
        List<Article> articles = pageArticles.getContent();
//        List<ArticleRequestDTO> articleRequestDTO = articles.stream().map(article -> modelMapper.map(article, ArticleRequestDTO.class))
//                .collect(Collectors.toList());
        DataTableResponseDTO<Article> articleResponse = new DataTableResponseDTO<>();
        articleResponse.setContent(articles);
        articleResponse.setPageNumber(pageArticles.getNumber());
        articleResponse.setPageSize(pageArticles.getSize());
        articleResponse.setTotalElements(pageArticles.getTotalElements());
        articleResponse.setTotalPages(pageArticles.getTotalPages());
        articleResponse.setLastPage(pageArticles.isLast());

        return articleResponse;
    }

    @Override
    public Article getArticleById(Integer articleId) {
        return articleRepo.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + articleId));
    }

    @Override
    public Article createArticle(ArticleRequestDTO articleRequestDTO) {
        CategoryArticle categoryArticle = categoryArticleService.findCategoryArticleById(articleRequestDTO.getCategoryArticleId());
        Article article;
        article = modelMapper.map(articleRequestDTO, Article.class);
        article.setCategoryArticle(categoryArticle);
        String imageUrl = minIOService.uploadFile(articleRequestDTO.getImagePath());
        article.setImagePath(imageUrl);
        article.setCreatedAt(LocalDateTime.now());
        return articleRepo.save(article);
    }

    @Override
    public Article updateArticle(Integer articleId, ArticleRequestDTO articleRequestDTO) {
        Article existingArticle = getArticleById(articleId);
        CategoryArticle categoryArticle = categoryArticleService.findCategoryArticleById(articleRequestDTO.getCategoryArticleId());
        existingArticle.setCategoryArticle(categoryArticle);
        existingArticle.setName(articleRequestDTO.getName());
        existingArticle.setShortDescription(articleRequestDTO.getShortDescription());
        existingArticle.setContent(articleRequestDTO.getContent());
        if (articleRequestDTO.getImagePath() != null) {
            String imageUrl = minIOService.uploadFile(articleRequestDTO.getImagePath());
            existingArticle.setImagePath(imageUrl);
        }
        return articleRepo.save(existingArticle);
    }

    @Override
    public void deleteArticle(Integer articleId) {
        Article existingArticle = getArticleById(articleId);
        articleRepo.delete(existingArticle);
    }

}
