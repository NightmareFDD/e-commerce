package com.lumastyle.order.controller;

import com.lumastyle.order.dto.OrderRequest;
import com.lumastyle.order.dto.OrderResponse;
import com.lumastyle.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Received request to create an order: {}", request);
        OrderResponse response = orderService.createOrder(request);
        log.info("Returning response for order: {}", response.getOrderNumber());
        return response;
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        log.info("Received request to find an order for id: {}", id);
        OrderResponse response = orderService.getOrder(id);
        log.info("Returning order details for id: {}", id);
        return response;
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getUserOrders(@PathVariable Long userId) {
        log.info("Received request to find all orders for userId: {}", userId);
        List<OrderResponse> list = orderService.getUserOrders(userId);
        log.info("Returning {} orders for userId={}", list.size(), userId);
        return list;
    }
}

