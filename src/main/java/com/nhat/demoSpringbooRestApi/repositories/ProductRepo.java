package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

//    List<Product> findByName (String name);
    Page<Product> findByNameLike(String name, Pageable pageDetails);
    Page<Product> findByCategoryId(int categoryId, Pageable pageDetails);


}
