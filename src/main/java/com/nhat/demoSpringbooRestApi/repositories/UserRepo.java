package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    Page<User> findAll(Specification<User> spec, Pageable pageable);
    @Query("SELECT e FROM User e WHERE e.email LIKE  %:key% OR e.name LIKE %:key% ")
    Page<User> searchByNameOrEmail(@Param("key") String key, Pageable pageDetails);
//    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT e FROM User e JOIN e.roles r WHERE e.id = ?1 ")
    Optional<User> findById( Integer id);

}
