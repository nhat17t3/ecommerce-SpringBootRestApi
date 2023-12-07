package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderRequestDTO;
import com.nhat.demoSpringbooRestApi.models.EOrderStatus;
import com.nhat.demoSpringbooRestApi.models.EPaymentStatus;
import com.nhat.demoSpringbooRestApi.models.Order;

public interface OrderService {

    DataTableResponseDTO<Order> getAllOrders(OrderFilterRequestDTO orderFilterRequestDTO);
    Order getOrderById(Integer orderId);
    Order createOrder (OrderRequestDTO order) throws Exception;
    Order updateOrder(Integer orderId, OrderRequestDTO order);
    void deleteOrder (Integer orderId);
    String updateOrderStatus(Integer orderId, String status);
    public void updateAllOrderStatusFromShip24();
    public String updateTrackingNumberForOrder(int orderId, String trackingNumber);
    public void updatePaymentStatus(Integer orderId, EPaymentStatus paymentStatus);

}
