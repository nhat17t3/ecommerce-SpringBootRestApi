package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.OrderDetailRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.*;
import com.nhat.demoSpringbooRestApi.repositories.OrderDetailRepository;
import com.nhat.demoSpringbooRestApi.repositories.OrderRepository;
import com.nhat.demoSpringbooRestApi.services.OrderService;
import com.nhat.demoSpringbooRestApi.services.PaymentMethodService;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import com.nhat.demoSpringbooRestApi.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    @Autowired
    private ModelMapper modelMapper;

    @Value("${ship24.apiKey}")
    private String apiKey;


    @Autowired
    private WebClient ship24WebClient;

    @Autowired
    private  ShipmentService shipmentService;

    @Override
    public OrderListResponseDTO getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findAll(pageDetails);

        List<Order> orders = pageOrders.getContent();

//        List<OrderRequestDTO> orderRequestDTO = orders.stream().map(order -> modelMapper.map(order, OrderRequestDTO.class))
//                .collect(Collectors.toList());

        OrderListResponseDTO orderResponse = new OrderListResponseDTO();

        orderResponse.setContent(orders);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public OrderListResponseDTO searchOrderByUserEmail(String email, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findByUserEmail(email, pageDetails);

        List<Order> orders = pageOrders.getContent();

//        if (orders.size() == 0) {
//            throw new CustomException("Orders not found with category ID: " + categoryId);
//        }

//        List<OrderRequestDTO> orderRequestDTO = orders.stream().map(order -> modelMapper.map(order, OrderRequestDTO.class))
//                .collect(Collectors.toList());

        OrderListResponseDTO orderResponse = new OrderListResponseDTO();

        orderResponse.setContent(orders);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public OrderListResponseDTO searchOrderByOrderStatus(EOrderStatus eOrderStatus, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Order> pageOrders = orderRepo.findByOrderStatus(eOrderStatus, pageDetails);

        List<Order> orders = pageOrders.getContent();

//        if (orders.size() == 0) {
//            throw new CustomException("Orders not found with keyword: " + keyword);
//        }

//        List<OrderRequestDTO> orderRequestDTO = orders.stream().map(order -> modelMapper.map(order, OrderRequestDTO.class))
//                .collect(Collectors.toList());

        OrderListResponseDTO orderResponse = new OrderListResponseDTO();

        orderResponse.setContent(orders);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());

        return orderResponse;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if(order.getTrackingNumber() != null){
            shipmentService.updateOrderStatusFromShip24(order.getTrackingNumber());
        }
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        User user = userService.getUserById(orderRequestDTO.getUserId());
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(orderRequestDTO.getPaymentMethodId());

        Order order = modelMapper.map(orderRequestDTO, Order.class);
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        Order order1 = orderRepo.save(order);

        for (OrderDetailRequestDTO orderDetailRequestDTO: orderRequestDTO.getOrderDetailRequestDTO()) {
            Product product = productService.getProductById(orderDetailRequestDTO.getProductId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setOrder(order1);
            orderDetail.setQuantity(orderDetailRequestDTO.getQuantity());
            orderDetail.setPrice(orderDetailRequestDTO.getPrice());
            orderDetailRepository.save(orderDetail);
        }



        return order1;
    }

    @Override
    public Order updateOrder(Integer orderId, OrderRequestDTO orderRequestDTO) {
        Order order = getOrderById(orderId);
        User user = userService.getUserById(orderRequestDTO.getUserId());
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(orderRequestDTO.getPaymentMethodId());

//        order = modelMapper.map(orderRequestDTO, Order.class);
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);

        order.setOrderStatus(orderRequestDTO.getOrderStatus());
        order.setTotalPrice(orderRequestDTO.getTotalPrice());
        order.setPaymentStatus(orderRequestDTO.getPaymentStatus());
        order.setNameReceiver(orderRequestDTO.getNameReceiver());
        order.setPhoneReceiver(orderRequestDTO.getPhoneReceiver());
        order.setAddressReceiver(orderRequestDTO.getAddressReceiver());
        order.setUpdatedAt(LocalDateTime.now());

        orderDetailRepository.deleteOrderDetailByOrderId(order.getId());
        for (OrderDetailRequestDTO orderDetailRequestDTO: orderRequestDTO.getOrderDetailRequestDTO()) {
            Product product = productService.getProductById(orderDetailRequestDTO.getProductId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setQuantity(orderDetailRequestDTO.getQuantity());
            orderDetail.setPrice(orderDetailRequestDTO.getPrice());
            orderDetailRepository.save(orderDetail);
        }

        return orderRepo.save(order);
    }

    @Override
    public String deleteOrder(Integer orderId) {
        Order existingOrder = getOrderById(orderId);
        orderRepo.delete(existingOrder);
        return "Order with orderId: " + orderId + " deleted successfully !!!";

    }

    @Override
    public String updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setOrderStatus(status);
        return "update order-status success";
    }

    @Override
    public void updateAllOrderStatusFromShip24() {
        List<Order> orders = orderRepo.findAllByOrderStatus("pending");
        for (Order order: orders) {
            if(order.getTrackingNumber() != null){
                shipmentService.updateOrderStatusFromShip24(order.getTrackingNumber());
            }
        }
    }

    @Override
    public String updateTrackingNumberForOrder(int orderId, String trackingNumber) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setTrackingNumber(trackingNumber);
        shipmentService.createTracker(trackingNumber);
        return "update tracking-number success";
    }

    @Override
    public void updatePaymentStatus(Integer orderId, String paymentStatus) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setPaymentStatus(paymentStatus);
        orderRepo.save(order);
    }


}
