package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.PaymentMethodRequestDTO;
import com.nhat.demoSpringbooRestApi.models.PaymentMethod;
import com.nhat.demoSpringbooRestApi.services.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paymentMethods")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethods();
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.getAll", paymentMethods);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findPaymentMethodById(@PathVariable("id") int id) {
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(id);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.getById", paymentMethod);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPaymentMethod(@Valid @RequestBody PaymentMethodRequestDTO requestDTO) {
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(requestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.create", createdPaymentMethod);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePaymentMethod(@PathVariable int id, @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(id, paymentMethodRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.update", updatedPaymentMethod);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePaymentMethod(@PathVariable int id) {
        paymentMethodService.deletePaymentMethod(id);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.delete");
        return ResponseEntity.status(200).body(baseResponse);
    }


}
