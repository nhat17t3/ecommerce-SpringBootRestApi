package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface CategoryService{

    List<Category> getAllCategories ();

    Category findCategoryById(int categoryId);

    List<Category> findCategoryByName (String name);

    Category createCategory (Category category);

    Category updateCategory (int categoryId, Category category);

    String deleteCategory (int categoryId);
}
