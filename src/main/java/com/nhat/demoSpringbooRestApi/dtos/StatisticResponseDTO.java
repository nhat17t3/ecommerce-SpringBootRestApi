package com.nhat.demoSpringbooRestApi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticResponseDTO {

    private Long amountOfUser;

    private Long amountOfProduct;

    private Long amountOfOrder;

    private Double totalRevenue;

    private List<Double> totalRevenueOfMonths;

}
