package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.StatisticResponseDTO;
import com.nhat.demoSpringbooRestApi.models.Order;
import com.nhat.demoSpringbooRestApi.repositories.OrderRepository;
import com.nhat.demoSpringbooRestApi.repositories.ProductRepo;
import com.nhat.demoSpringbooRestApi.repositories.UserRepo;
import com.nhat.demoSpringbooRestApi.services.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public StatisticResponseDTO getStatisticsShop() {
        StatisticResponseDTO statisticResponseDTO = new StatisticResponseDTO();
        statisticResponseDTO.setAmountOfUser(userRepo.count());
        statisticResponseDTO.setAmountOfProduct(productRepo.count());
        statisticResponseDTO.setAmountOfOrder(orderRepository.count());
        double totalRevenue = 0;
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getOrderStatus().equalsIgnoreCase("delivered")) totalRevenue += order.getTotalPrice();
        }
        statisticResponseDTO.setTotalRevenue(totalRevenue);
        return statisticResponseDTO;
    }

    @Override
    public List<Double> getRevenueByMonths(int year) {
        List<Double> listRevenueByMonths = new ArrayList<>();
        for (int i = 1; i <= 12 ; i++) {
            int day = 31;
            if(i == 2) day = 28;
            if( i == 4 || i== 6 || i==9 || i== 11) day = 30;

            LocalDateTime start = LocalDateTime.of(year, i, 1, 1, 1);
            LocalDateTime end = LocalDateTime.of(year, i, day, 1, 1);
            double totalRevenueByMonth = 0;
            List<Order> orders = orderRepository.findAll();
            for (Order order : orders) {
                if (order.getOrderStatus().equalsIgnoreCase("delivered") && order.getCreatedAt().isBefore(end) && order.getCreatedAt().isAfter(start))
                    totalRevenueByMonth += order.getTotalPrice();
            }
            listRevenueByMonths.add(totalRevenueByMonth);
        }
        return listRevenueByMonths;
    }
}
