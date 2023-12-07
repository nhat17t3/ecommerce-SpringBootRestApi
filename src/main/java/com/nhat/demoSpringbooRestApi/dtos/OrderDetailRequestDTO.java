package com.nhat.demoSpringbooRestApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDTO {

    private int orderId;

    @NotNull
    private Integer productId;

    @NotNull
    private Float price;

    @NotNull
    private Integer quantity;
}