package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Category;
import com.nhat.demoSpringbooRestApi.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Integer> {

}
