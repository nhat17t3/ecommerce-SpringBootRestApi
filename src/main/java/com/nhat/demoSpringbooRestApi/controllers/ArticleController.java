package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.ArticleFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.ArticleRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.models.Article;
import com.nhat.demoSpringbooRestApi.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllArticles(ArticleFilterRequestDTO articleFilterRequestDTO) {
        DataTableResponseDTO<Article> articles = articleService.getAllArticles(articleFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("article.success.getAll", articles);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<BaseResponse> getArticleById(@PathVariable Integer articleId) {
        Article article = articleService.getArticleById(articleId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("article.success.getById", article);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> addArticle(ArticleRequestDTO articleRequestDTO) {
        Article savedArticle = articleService.createArticle(articleRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("article.success.create-article", savedArticle);
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<BaseResponse> updateArticle(@PathVariable Integer articleId,
                                                      @ModelAttribute ArticleRequestDTO articleRequestDTO) {
        Article savedArticle = articleService.updateArticle(articleId, articleRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("article.success.update-article", savedArticle);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<BaseResponse> deleteArticle(@PathVariable Integer articleId) {
        articleService.deleteArticle(articleId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("article.success.delate-article");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
