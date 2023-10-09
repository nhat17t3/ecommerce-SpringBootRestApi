package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.CategoryRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.repositories.CategoryRepo;
import com.nhat.demoSpringbooRestApi.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category findCategoryById(int categoryId) {
        return  categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

    @Override
    public List<Category> findCategoryByName(String name) {
        return categoryRepo.findByNameLike(name);
    }

    @Override
    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = modelMapper.map(categoryRequestDTO, Category.class);
        return categoryRepo.save(category);
    }

    @Override
    public Category updateCategory(int categoryId, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = findCategoryById(categoryId);
        existingCategory.setName(categoryRequestDTO.getName());
        existingCategory.setCode(categoryRequestDTO.getCode());
        return categoryRepo.save(existingCategory);
    }

    @Override
    public String deleteCategory(int categoryId) {
        Category existingCategory = findCategoryById(categoryId);
        categoryRepo.delete(existingCategory);
        return "Categoty has id =" + categoryId + ": is deleted";
    }
}
