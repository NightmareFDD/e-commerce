package com.lumastyle.order.mapper;

import com.lumastyle.order.dto.AddressRequest;
import com.lumastyle.order.dto.OrderItemRequest;
import com.lumastyle.order.dto.OrderRequest;
import com.lumastyle.order.dto.OrderResponse;
import com.lumastyle.order.entity.*;

import java.util.List;

/**
 * Mapper interface for converting between Order-related DTOs and entities.
 */
public interface OrderMapper {

    /**
     * Convert an OrderRequest DTO into an Order entity.
     *
     * @param request the incoming order request
     * @return the populated Order entity
     */
    Order toEntity(OrderRequest request);

    /**
     * Convert an OrderItemRequest DTO into an OrderItem entity linked to a specific order.
     *
     * @param dto   the incoming order item request
     * @param order the parent Order entity
     * @return the populated OrderItem entity
     */
    OrderItem toEntity(OrderItemRequest dto, Order order);

    /**
     * Convert an AddressRequest DTO into an Address entity linked to a specific order.
     *
     * @param dto   the incoming address request
     * @param order the parent Order entity
     * @param type  the type of address (BILLING or SHIPPING)
     * @return the populated Address entity
     */
    Address toAddress(AddressRequest dto, Order order, AddressType type);

    /**
     * Create an OrderPayment entity from the order request and parent order.
     *
     * @param request the incoming order request
     * @param order   the parent Order entity
     * @return the populated OrderPayment entity containing financial details
     */
    OrderPayment toPayment(OrderRequest request, Order order);

    /**
     * Convert Order entity and associated child entities into an OrderResponse DTO.
     *
     * @param order     the Order entity
     * @param items     the list of OrderItem entities
     * @param addresses the list of Address entities
     * @param payment   the OrderPayment entity
     * @return the populated OrderResponse DTO
     */
    OrderResponse toResponse(Order order, List<OrderItem> items, List<Address> addresses, OrderPayment payment);

}
