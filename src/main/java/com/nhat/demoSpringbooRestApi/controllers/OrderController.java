package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.configs.AppConstants;
import com.nhat.demoSpringbooRestApi.dtos.OrderListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.OrderRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Order;
import com.nhat.demoSpringbooRestApi.services.OrderService;
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

}
