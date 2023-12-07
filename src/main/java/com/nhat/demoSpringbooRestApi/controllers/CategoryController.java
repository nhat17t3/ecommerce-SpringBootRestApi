package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.CategoryResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.CategoryRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.services.CategoryService;
import com.nhat.demoSpringbooRestApi.services.impl.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = Logger.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EmailService emailService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllCategories() throws Exception {
        List<Category> categories = categoryService.getAllCategories();
        BaseResponse baseResponse = new BaseResponse(true,"category.success.getAll",categories,null);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{id_1}")
    public ResponseEntity<BaseResponse> findCategoryById(@PathVariable("id_1") int id) {
        Category category = categoryService.findCategoryById(id);
        BaseResponse baseResponse = new BaseResponse(true,"category.success.findCategoryById",category,null);
        return ResponseEntity.status(200).body(baseResponse);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<Category>> findCategoryByName(@RequestParam(value="name", required=true) String key) {
//        List<Category> categories = categoryService.findCategoryByName(key);
//        return ResponseEntity.ok(categories);
//    }

    @PostMapping
    public ResponseEntity<BaseResponse> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        Category createdCategory = categoryService.createCategory(requestDTO);
        BaseResponse baseResponse = new BaseResponse(true,"category.success.createdCategory",createdCategory,null);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryRequestDTO);
        BaseResponse baseResponse = new BaseResponse(true,"category.success.updatedCategory",updatedCategory,null);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable int id) {
        if(categoryService.checkBeforeDeleteCategory(id)){
            categoryService.deleteCategory(id);
            BaseResponse baseResponse = new BaseResponse(true,"category.success.deleteCategory",null,null);
            return ResponseEntity.status(200).body(baseResponse);
        }
        else {
            BaseResponse baseResponse = new BaseResponse(false,"category.error.deleteCategory.existProduct",null,null);
            return ResponseEntity.status(200).body(baseResponse);
        }
    }

}
