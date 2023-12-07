package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.PaymentMethodRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.PaymentMethod;
import com.nhat.demoSpringbooRestApi.repositories.PaymentMethodRepository;
import com.nhat.demoSpringbooRestApi.services.PaymentMethodService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepo.findAll();
    }

    @Override
    public PaymentMethod findPaymentMethodById(int paymentMethodId) {
        return  paymentMethodRepo.findById(paymentMethodId)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod not found with id: " + paymentMethodId));
    }


    @Override
    public PaymentMethod createPaymentMethod(PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodRequestDTO, PaymentMethod.class);
        return paymentMethodRepo.save(paymentMethod);
    }

    @Override
    public PaymentMethod updatePaymentMethod(int paymentMethodId, PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod existingPaymentMethod = findPaymentMethodById(paymentMethodId);
        existingPaymentMethod.setName(paymentMethodRequestDTO.getName());
        existingPaymentMethod.setDescription(paymentMethodRequestDTO.getDescription());
        existingPaymentMethod.setIsEnable(paymentMethodRequestDTO.getIsEnable());
        return paymentMethodRepo.save(existingPaymentMethod);
    }

    @Override
    public void deletePaymentMethod(int paymentMethodId) {
        PaymentMethod existingPaymentMethod = findPaymentMethodById(paymentMethodId);
        paymentMethodRepo.delete(existingPaymentMethod);
    }
}
