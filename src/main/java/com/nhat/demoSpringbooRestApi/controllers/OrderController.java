package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.configs.AppConstants;
import com.nhat.demoSpringbooRestApi.dtos.OrderListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderRequestDTO;
import com.nhat.demoSpringbooRestApi.dtos.PaymentRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Order;
import com.nhat.demoSpringbooRestApi.services.OrderService;
import com.nhat.demoSpringbooRestApi.services.impl.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayPalService payPalService;

    @GetMapping("")
    public ResponseEntity<OrderListResponseDTO> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        OrderListResponseDTO orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<OrderListResponseDTO>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        Order order = orderService.getOrderById(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Order> addOrder(@Valid @RequestBody OrderRequestDTO order) {
        Order savedOrder = orderService.createOrder(order);
        return new ResponseEntity<Order>(savedOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer orderId, @Valid @RequestBody OrderRequestDTO order) {
        Order savedOrder = orderService.updateOrder(orderId, order);
        return new ResponseEntity<Order>(savedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer orderId) {
        String message = orderService.deleteOrder(orderId);
        return new ResponseEntity<String>(message, HttpStatus.OK);
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
    public ResponseEntity<?> confirmPayment(@RequestParam String paymentId, @RequestParam String payerId , @RequestParam int orderId) {
        try {
            payPalService.confirmPayment(paymentId, payerId, orderId);
            return ResponseEntity.ok().build();
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        String message = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(message);

    }

    @PostMapping("/update-all-status-from-ship24")
    public ResponseEntity<String> updateAllOrderStatusFromShip24() {
        try {
            orderService.updateAllOrderStatusFromShip24();
            return ResponseEntity.ok("All orders updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating orders");
        }
    }

    @PutMapping("/{orderId}/tracking-number")
    public ResponseEntity<String> updateTrackingNumberForOrder(@PathVariable int orderId, @RequestParam String trackingNumber) {
        String message = orderService.updateTrackingNumberForOrder(orderId, trackingNumber);
        return ResponseEntity.ok(message);

    }

}
