package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.PaymentMethodRequestDTO;
import com.nhat.demoSpringbooRestApi.models.PaymentMethod;
import com.nhat.demoSpringbooRestApi.services.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paymentMethods")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("")
    public ResponseEntity<List<PaymentMethod>> getAllCategories() {
        List<PaymentMethod> categories = paymentMethodService.getAllPaymentMethods();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> findPaymentMethodById(@PathVariable("id") int id) {
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @PostMapping
    public ResponseEntity<PaymentMethod> createPaymentMethod(@Valid @RequestBody PaymentMethodRequestDTO requestDTO) {
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(requestDTO);
        return ResponseEntity.ok(createdPaymentMethod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable int id, @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(id, paymentMethodRequestDTO);
        return ResponseEntity.ok(updatedPaymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentMethod(@PathVariable int id) {
        String message = paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


}
