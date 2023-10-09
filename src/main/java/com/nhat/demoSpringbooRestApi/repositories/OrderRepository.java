package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByUserEmail(String email, Pageable pageDetails);
    Page<Order> findByOrderStatus(EOrderStatus eOrderStatus, Pageable pageDetails);
}
