package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.OrderListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderRequestDTO;
import com.nhat.demoSpringbooRestApi.models.EOrderStatus;
import com.nhat.demoSpringbooRestApi.models.Order;

public interface OrderService {

    OrderListResponseDTO getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    OrderListResponseDTO searchOrderByUserEmail(String email, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    OrderListResponseDTO searchOrderByOrderStatus(EOrderStatus eOrderStatus, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    Order getOrderById(Integer orderId);
    Order createOrder (OrderRequestDTO order);
    Order updateOrder(Integer orderId, OrderRequestDTO order);
    String deleteOrder (Integer orderId);
    String updateOrderStatus(Integer orderId, String status);
    public void updateAllOrderStatusFromShip24();
    public String updateTrackingNumberForOrder(int orderId, String trackingNumber);

    public void updatePaymentStatus(Integer orderId, String paymentStatus);
}
