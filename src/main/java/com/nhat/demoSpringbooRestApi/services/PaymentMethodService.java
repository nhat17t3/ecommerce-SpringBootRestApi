package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.PaymentMethodRequestDTO;
import com.nhat.demoSpringbooRestApi.models.PaymentMethod;

import java.util.List;


public interface PaymentMethodService {

    List<PaymentMethod> getAllPaymentMethods ();

    PaymentMethod findPaymentMethodById(int paymentMethodId);


    PaymentMethod createPaymentMethod (PaymentMethodRequestDTO paymentMethodRequestDTO);

    PaymentMethod updatePaymentMethod (int paymentMethodId, PaymentMethodRequestDTO paymentMethodRequestDTO);

    String deletePaymentMethod (int paymentMethodId);
}
