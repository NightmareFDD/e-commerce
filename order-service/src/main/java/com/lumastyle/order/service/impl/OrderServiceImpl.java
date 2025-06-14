package com.lumastyle.order.service.impl;

import com.lumastyle.order.dto.OrderRequest;
import com.lumastyle.order.dto.OrderResponse;
import com.lumastyle.order.entity.*;
import com.lumastyle.order.exception.OrderNotFoundException;
import com.lumastyle.order.mapper.OrderMapper;
import com.lumastyle.order.repository.AddressRepository;
import com.lumastyle.order.repository.OrderItemRepository;
import com.lumastyle.order.repository.OrderPaymentRepository;
import com.lumastyle.order.repository.OrderRepository;
import com.lumastyle.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderMapper mapper;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating a new order for user {}", request.getUserId());
        Order order = mapper.toEntity(request);
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with id: {} and number: {}", savedOrder.getId(), savedOrder.getOrderNumber());

        List<OrderItem> items = request.getItems().stream()
                .map(dto -> mapper.toEntity(dto, savedOrder))
                .map(orderItemRepository::save)
                .collect(Collectors.toList());
        log.info("Saved {} items for orderId: {}", items.size(), savedOrder.getId());

        List<Address> addresses = List.of(
                addressRepository.save(mapper.toAddress(request.getBillingAddress(), order, AddressType.BILLING)),
                addressRepository.save(mapper.toAddress(request.getShippingAddress(), order, AddressType.SHIPPING))
        );
        log.info("Saved {} addresses for orderId:{}", addresses.size(), savedOrder.getId());

        OrderPayment payment = mapper.toPayment(request, order);
        payment = orderPaymentRepository.save(payment);
        log.info("Saved payment info for orderId:{}", savedOrder.getId());

        log.info("Order {} created successfully", savedOrder.getOrderNumber());
        return mapper.toResponse(order, items, addresses, payment);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        log.info("Order found with id: {} and number: {}", order.getId(), order.getOrderNumber());
        List<OrderItem> items = orderItemRepository.findByOrderId(id);
        List<Address> addresses = addressRepository.findByOrderId(id);
        OrderPayment payment = orderPaymentRepository.findByOrder(order);
        return mapper.toResponse(order, items, addresses, payment);
    }

    @Override
    @Cacheable(value = "ordersByUser", key = "#userId")
    public List<OrderResponse> getUserOrders(Long userId) {
        log.info("Fetching orders for userId: {}", userId);

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.info("Found {} orders for userId: {}", orders.size(), userId);

        List<OrderResponse> responses = orders.stream()
                .map(order -> {
                    List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
                    List<Address> addresses = addressRepository.findByOrderId(order.getId());
                    OrderPayment payment = orderPaymentRepository.findByOrder(order);
                    return mapper.toResponse(order, items, addresses, payment);
                })
                .collect(Collectors.toList());
        log.info("Found all orders completed for userId: {}, returned {} responses", userId, responses.size());

        return responses;
    }
}
