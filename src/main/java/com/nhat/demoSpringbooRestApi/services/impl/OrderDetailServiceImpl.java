//package com.nhat.demoSpringbooRestApi.services.impl;
//
//import com.nhat.demoSpringbooRestApi.dtos.OrderDetailRequestDTO;
//import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
//import com.nhat.demoSpringbooRestApi.models.OrderDetail;
//import com.nhat.demoSpringbooRestApi.models.Product;
//import com.nhat.demoSpringbooRestApi.models.User;
//import com.nhat.demoSpringbooRestApi.models.UserProductKey;
//import com.nhat.demoSpringbooRestApi.repositories.OrderDetailRepository;
//import com.nhat.demoSpringbooRestApi.services.OrderDetailService;
//import com.nhat.demoSpringbooRestApi.services.ProductService;
//import com.nhat.demoSpringbooRestApi.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class OrderDetailServiceImpl implements OrderDetailService {
//
//    @Autowired
//    OrderDetailRepository orderDetailRepo;
//
//    @Autowired
//    OrderService orderService;
//
//    @Autowired
//    ProductService productService;
//
//    @Override
//    public List<OrderDetail> getAllOrderDetails() {
//        return orderDetailRepo.findAll();
//    }
//
//    @Override
//    public OrderDetail findOrderDetailById(UserProductKey orderDetailId) {
//        return orderDetailRepo.findById(orderDetailId).orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id: " + orderDetailId));
//    }
//
//    @Override
//    public List<OrderDetail> findAllOrderDetailByProductId(int productId) {
//        return orderDetailRepo.findAllByProductId(productId);
//    }
//
//    @Override
//    public OrderDetail createOrderDetail(OrderDetailRequestDTO orderDetail) {
//        Product product = productService.getProductById(orderDetail.getProductId());
//        User user = userService.getUserById((orderDetail.getUserId()));
//
//        OrderDetail newOrderDetail = new OrderDetail();
//        newOrderDetail.setContent(orderDetail.getContent());
//        newOrderDetail.setCreatedAt(LocalDateTime.now());
//        newOrderDetail.setProduct(product);
//        newOrderDetail.setUser(user);
//
//        return orderDetailRepo.save(newOrderDetail);
//    }
//
//    @Override
//    public OrderDetail updateOrderDetail( OrderDetailRequestDTO orderDetail) {
//        UserProductKey userProductKey = new UserProductKey(orderDetail.getUserId(), orderDetail.getProductId());
//        OrderDetail updateOrderDetail = findOrderDetailById(userProductKey);
//
////        Product product = productService.getProductById(orderDetail.getProductId());
////        User user = userService.getUserById((orderDetail.getUserId()));
//        updateOrderDetail.setContent(orderDetail.getContent());
//        updateOrderDetail.setUpdatedAt(LocalDateTime.now());
////        updateOrderDetail.setProduct(product);
////        updateOrderDetail.setUser(user);
//
//        return orderDetailRepo.save(updateOrderDetail);
//    }
//
//    @Override
//    public String deleteOrderDetail(UserProductKey orderDetailId) {
//        OrderDetail existingOrderDetail = findOrderDetailById(orderDetailId);
//        orderDetailRepo.delete(existingOrderDetail);
//        return "OrderDetail with OrderDetailID: " + orderDetailId + " deleted successfully !!!";
//    }
//}
