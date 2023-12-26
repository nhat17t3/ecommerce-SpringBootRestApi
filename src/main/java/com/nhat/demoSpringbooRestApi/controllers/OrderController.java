package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.*;
import com.nhat.demoSpringbooRestApi.models.Order;
import com.nhat.demoSpringbooRestApi.services.OrderService;
import com.nhat.demoSpringbooRestApi.services.impl.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllOrders(OrderFilterRequestDTO orderFilterRequestDTO) {
        DataTableResponseDTO<Order> orders = orderService.getAllOrders(orderFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.getAll", orders);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponse> getOrderById(@PathVariable Integer orderId) {
        Order order = orderService.getOrderById(orderId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.getById", order);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping("")
    public ResponseEntity<BaseResponse> addOrder(@Valid @RequestBody OrderRequestDTO order) throws Exception {
        Order savedOrder = orderService.createOrder(order);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.create", savedOrder);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse> updateOrder(@PathVariable Integer orderId, @Valid @RequestBody OrderRequestDTO order) {
        Order savedOrder = orderService.updateOrder(orderId, order);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.update", savedOrder);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse> deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.delete");
        return ResponseEntity.status(200).body(baseResponse);
    }


    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        try {
            Payment payment = payPalService.createPayment(paymentRequest.getTotal(),
                    paymentRequest.getCurrency(),
                    paymentRequest.getMethod(),
                    paymentRequest.getIntent(),
                    paymentRequest.getDescription(),
                    paymentRequest.getCancelUrl(),
                    paymentRequest.getSuccessUrl());

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
            return ResponseEntity.badRequest().body("Cannot create payment");

        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestParam String paymentId, @RequestParam String payerId, @RequestParam int orderId) {
        try {
            payPalService.confirmPayment(paymentId, payerId, orderId);
            return ResponseEntity.ok().build();
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<BaseResponse> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        String message = orderService.updateOrderStatus(orderId, status);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse(message);
        return ResponseEntity.status(200).body(baseResponse);

    }

    @PostMapping("/update-all-status-from-tracking")
    public ResponseEntity<BaseResponse> updateAllOrderStatusFromTracking() {
        try {
            orderService.updateAllOrderStatusFromTracking();
            BaseResponse baseResponse = BaseResponse.createSuccessResponse("All orders updated successfully from tracking");
            return ResponseEntity.status(200).body(baseResponse);
        } catch (Exception e) {
            BaseResponse baseResponse = BaseResponse.createErrorResponse("Error updating orders from tracking");
            return ResponseEntity.status(500).body(baseResponse);
        }
    }

    @PutMapping("/{orderId}/tracking")
    public ResponseEntity<BaseResponse> updateTrackingNumberForOrder(@PathVariable int orderId, @RequestParam String trackingNumber, @RequestParam String courierCode) {
        String message = orderService.updateTrackingNumberForOrder(orderId, trackingNumber, courierCode);
        BaseResponse baseResponse = BaseResponse.createErrorResponse(message);
        return ResponseEntity.status(200).body(baseResponse);

    }

}
