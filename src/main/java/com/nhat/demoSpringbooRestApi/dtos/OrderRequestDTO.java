package com.nhat.demoSpringbooRestApi.dtos;

import com.nhat.demoSpringbooRestApi.models.EOrderStatus;
import com.nhat.demoSpringbooRestApi.models.EPaymentStatus;
import com.nhat.demoSpringbooRestApi.models.PaymentMethod;
import com.nhat.demoSpringbooRestApi.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull
    private int userId;

    @NotNull
    private Float totalPrice;

    @NotNull
    private EOrderStatus orderStatus;

    @NotNull
    private int paymentMethodId;

    @NotNull
    private EPaymentStatus paymentStatus;

    @NotNull
    private String nameReceiver;

    @NotNull
    private String phoneReceiver;

    @NotNull
    private String addressReceiver;

    @NotNull
    private Set<OrderDetailRequestDTO> orderDetailRequestDTO;

}
