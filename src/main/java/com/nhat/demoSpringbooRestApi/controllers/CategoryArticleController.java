package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.CategoryArticleRequestDTO;
import com.nhat.demoSpringbooRestApi.models.CategoryArticle;
import com.nhat.demoSpringbooRestApi.services.CategoryArticleService;
import com.nhat.demoSpringbooRestApi.services.impl.EmailService;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorieArticles")
public class CategoryArticleController {

    private static final Logger logger = Logger.getLogger(CategoryArticleController.class);
    @Autowired
    private CategoryArticleService categoryArticleService;

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllCategories() throws Exception {
        List<CategoryArticle> categories = categoryArticleService.getAllCategoryArticles();
        BaseResponse baseResponse = new BaseResponse(true, "categoryArticle.success.getAll", categories, null);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{id_1}")
    public ResponseEntity<BaseResponse> findCategoryArticleById(@PathVariable("id_1") int id) {
        CategoryArticle categoryArticle = categoryArticleService.findCategoryArticleById(id);
        BaseResponse baseResponse = new BaseResponse(true, "categoryArticle.success.findCategoryArticleById", categoryArticle, null);
        return ResponseEntity.status(200).body(baseResponse);
    }


    @PostMapping
    public ResponseEntity<BaseResponse> createCategoryArticle(@Valid @RequestBody CategoryArticleRequestDTO requestDTO) {
        CategoryArticle createdCategoryArticle = categoryArticleService.createCategoryArticle(requestDTO);
        BaseResponse baseResponse = new BaseResponse(true, "categoryArticle.success.createdCategoryArticle", createdCategoryArticle, null);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateCategoryArticle(@PathVariable int id, @Valid @RequestBody CategoryArticleRequestDTO categoryArticleRequestDTO) {
        CategoryArticle updatedCategoryArticle = categoryArticleService.updateCategoryArticle(id, categoryArticleRequestDTO);
        BaseResponse baseResponse = new BaseResponse(true, "categoryArticle.success.updatedCategoryArticle", updatedCategoryArticle, null);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCategoryArticle(@PathVariable int id) {
        if (categoryArticleService.checkBeforeDeleteCategoryArticle(id)) {
            categoryArticleService.deleteCategoryArticle(id);
            BaseResponse baseResponse = new BaseResponse(true, "categoryArticle.success.deleteCategoryArticle", null, null);
            return ResponseEntity.status(200).body(baseResponse);
        } else {
            BaseResponse baseResponse = new BaseResponse(false, "categoryArticle.error.deleteCategoryArticle.existProduct", null, null);
            return ResponseEntity.status(200).body(baseResponse);
        }
    }

}
