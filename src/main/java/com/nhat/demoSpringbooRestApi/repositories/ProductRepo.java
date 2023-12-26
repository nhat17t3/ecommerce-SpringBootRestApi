package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {

//    List<Product> findByName (String name);
//    Page<Product> findByNameLike(String name, Pageable pageDetails);
//    Page<Product> findByCategoryId(int categoryId, Pageable pageDetails);
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
    @Query("SELECT product FROM Product product "
            + "JOIN FETCH product.category category "
            + "WHERE category.id = ?1")
    List<Product> checkBeforeDeleteCategory(int categoryId);
}
