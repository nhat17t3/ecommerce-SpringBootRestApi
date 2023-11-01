package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image,Integer> {
    List<Image> findByProductId(Integer productId);
    Image findByProductIdAndIsPrimary(Integer productId, boolean isPrimary);
}
