package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.TrackingOrderRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Order;
import com.nhat.demoSpringbooRestApi.repositories.OrderRepository;
import com.nhat.demoSpringbooRestApi.services.OrderService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ShipmentService {

    @Value("${ship24.apiKey}")
    private String apiKey;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WebClient ship24WebClient;

    @Autowired
    private OrderRepository orderRepository;

    public void updateOrderStatusFromShip24(String trackingNumber) {
        String response = ship24WebClient.get()
                .uri("https://api.ship24.com/public/v1/trackers/search/{trackingNumber}/results", trackingNumber)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Phân tích dữ liệu từ response và cập nhật
        JSONObject jsonObject = new JSONObject(response);
        String status = jsonObject.getJSONObject("data").getString("status");

        Order order = orderRepository.findByTrackingNumber(trackingNumber).orElse(null);
        if (order != null) {
            order.setOrderStatus(status);
            orderRepository.save(order);
        }
    }

    public void createTracker(String trackingNumber) {
        TrackingOrderRequestDTO trackingOrderRequestDTO = new TrackingOrderRequestDTO();
        trackingOrderRequestDTO.setTrackingNumber(trackingNumber);
        try {
            String response = ship24WebClient.post()
                    .uri("https://api.ship24.com/public/v1/trackers")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(Mono.just(trackingOrderRequestDTO),TrackingOrderRequestDTO.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse và xử lý dữ liệu từ response ở đây
            // Ví dụ:
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getJSONObject("data").getString("status");

        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý các ngoại lệ phát sinh ở đây
        }


    }



}