package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    Page<Order> findAll(Specification<Order> spec, Pageable pageable);
    Page<Order> findByUserEmail(String email, Pageable pageDetails);
    Page<Order> findByOrderStatus(EOrderStatus eOrderStatus, Pageable pageDetails);
    Optional<Order> findByTrackingNumber(String trackingNumber);
    @Query("SELECT order FROM Order order "
            + "WHERE order.orderStatus NOT IN ?1")
    List<Order> findAllByOrderNotInStatus(String[] orderStatus);

}
