package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.CategoryResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.CategoryRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id_1}")
    public ResponseEntity<CategoryResponseDTO> findCategoryById(@PathVariable("id_1") int id) {
        Category category = categoryService.findCategoryById(id);
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(category.getId(), category.getName());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Category>> findCategoryByName(@RequestParam(value="name", required=true) String key) {
        List<Category> categories = categoryService.findCategoryByName(key);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        Category createdCategory = categoryService.createCategory(requestDTO);
        CategoryResponseDTO responseDTO = new CategoryResponseDTO(createdCategory.getId(), createdCategory.getName());
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryRequestDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        String message = categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


}
