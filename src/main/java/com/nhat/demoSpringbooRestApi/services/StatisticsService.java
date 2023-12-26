package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.StatisticResponseDTO;

import java.util.List;

public interface StatisticsService {
    public StatisticResponseDTO getStatisticsShop();

    public List<Double> getRevenueByMonths(int year);

}
