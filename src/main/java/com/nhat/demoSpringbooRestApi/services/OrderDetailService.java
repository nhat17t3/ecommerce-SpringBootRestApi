package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.OrderDetailRequestDTO;
import com.nhat.demoSpringbooRestApi.models.OrderDetail;
import com.nhat.demoSpringbooRestApi.models.UserProductKey;

import java.util.List;


public interface OrderDetailService {

    List<OrderDetail> getAllOrderDetails ();

    OrderDetail findOrderDetailById(UserProductKey orderDetailId);

    List<OrderDetail> findAllOrderDetailByProductId (int productId);

    OrderDetail createOrderDetail (OrderDetailRequestDTO orderDetail);

    OrderDetail updateOrderDetail ( OrderDetailRequestDTO orderDetail);

    String deleteOrderDetail (UserProductKey orderDetailId);

    void deleteOrderDetailByOrderId (int orderId);

}
