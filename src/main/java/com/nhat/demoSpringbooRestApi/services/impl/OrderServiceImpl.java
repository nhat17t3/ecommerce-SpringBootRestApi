package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.DataTableResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderDetailRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderFilterRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.*;
import com.nhat.demoSpringbooRestApi.repositories.OrderDetailRepository;
import com.nhat.demoSpringbooRestApi.repositories.OrderRepository;
import com.nhat.demoSpringbooRestApi.services.OrderService;
import com.nhat.demoSpringbooRestApi.services.PaymentMethodService;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import com.nhat.demoSpringbooRestApi.services.UserService;
import com.trackingmore.model.tracking.Tracking;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Value("${trackingMore.apiKey}")
    private String apiKey;


    @Autowired
    private TrackingShipmentService shipmentService;
    @Autowired
    private EmailService emailService;

    @Override
    public DataTableResponseDTO<Order> getAllOrders(OrderFilterRequestDTO orderFilterRequestDTO) {
        Sort sortByAndOrder = orderFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(orderFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(orderFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(orderFilterRequestDTO.getPageNumber(), orderFilterRequestDTO.getPageSize(), sortByAndOrder);
        Page<Order> pageOrders = orderRepo.findAll(pageDetails);
        List<Order> orders = pageOrders.getContent();
        DataTableResponseDTO<Order> orderResponse = new DataTableResponseDTO<Order>();
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
        if (order.getTrackingNumber() != null) {
            Tracking tracking = shipmentService.getTrackingByTrackingNumber(order.getTrackingNumber());
            order.setOrderStatus(tracking.getDeliveryStatus());
//            order.setTrackInfo(tracking.getOriginInfo().getTrackinfo());
            orderRepo.save(order);
        }
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public Order createOrder(OrderRequestDTO orderRequestDTO) throws Exception {
        User user = userService.getUserById(orderRequestDTO.getUserId());
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(orderRequestDTO.getPaymentMethodId());
        Order order = modelMapper.map(orderRequestDTO, Order.class);
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        order.setCreatedAt(LocalDateTime.now());
        Order newOrder = orderRepo.save(order);

        for (OrderDetailRequestDTO orderDetailRequestDTO : orderRequestDTO.getOrderDetailRequestDTO()) {
            Product product = productService.getProductById(orderDetailRequestDTO.getProductId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setOrder(newOrder);
            orderDetail.setQuantity(orderDetailRequestDTO.getQuantity());
            orderDetail.setPrice(orderDetailRequestDTO.getPrice());
            orderDetailRepository.save(orderDetail);
        }

        emailService.sendEmailFromTemplate(newOrder.getUser().getEmail(), "mail-order", "Bạn đã đặt hàng thành công từ shop HLN", newOrder);
        return newOrder;
    }

    @Override
    public Order updateOrder(Integer orderId, OrderRequestDTO orderRequestDTO) {
        Order order = getOrderById(orderId);
        User user = userService.getUserById(orderRequestDTO.getUserId());
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(orderRequestDTO.getPaymentMethodId());
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
        for (OrderDetailRequestDTO orderDetailRequestDTO : orderRequestDTO.getOrderDetailRequestDTO()) {
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
    public void deleteOrder(Integer orderId) {
        Order existingOrder = getOrderById(orderId);
        orderRepo.delete(existingOrder);
    }

    @Override
    public void updatePaymentStatus(Integer orderId, EPaymentStatus paymentStatus) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setPaymentStatus(paymentStatus);
        orderRepo.save(order);
    }

    @Override
    public String updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setOrderStatus(status);
        return "update order-status success";
    }

    @Override
    public void updateAllOrderStatusFromTracking() {
        List<Order> orders = orderRepo.findAllByOrderNotInStatus(new String[]{"delivered", "canceled"});
        for (Order order : orders) {
            if (order.getTrackingNumber() != null) {
                Tracking tracking = shipmentService.getTrackingByTrackingNumber(order.getTrackingNumber());
                order.setOrderStatus(tracking.getDeliveryStatus());
                orderRepo.save(order);
            }
        }
    }

    @Override
    public String updateTrackingNumberForOrder(int orderId, String trackingNumber, String courierCode) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setTrackingNumber(trackingNumber);
        orderRepo.save(order);
        shipmentService.createTracking(trackingNumber, courierCode);
        return "update tracking-number success";
    }

}
