package com.lumastyle.order.service;

import com.lumastyle.order.dto.OrderRequest;
import com.lumastyle.order.dto.OrderResponse;

import java.util.List;

/**
 * Service interface defining operations for managing orders.
 */
public interface OrderService {

    /**
     * Create a new order based on the provided request.
     *
     * @param request the order creation request
     * @return the created order as a response DTO
     */
    OrderResponse createOrder(OrderRequest request);

    /**
     * Retrieve a single order by its unique identifier.
     *
     * @param id the ID of the order to retrieve
     * @return the order as a response DTO
     */
    OrderResponse getOrder(Long id);

    /**
     * Retrieve all orders for a given user, sorted by creation date descending.
     *
     * @param userId the ID of the user whose orders are to be retrieved
     * @return list of order response DTOs
     */
    List<OrderResponse> getUserOrders(Long userId);
}