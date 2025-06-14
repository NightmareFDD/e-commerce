package com.lumastyle.order.mapper;

import com.lumastyle.order.dto.*;
import com.lumastyle.order.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus(OrderStatus.PENDING);
        order.setContactEmail(request.getContactEmail());
        order.setContactPhone(request.getContactPhone());
        order.setCreatedAt(Instant.now());
        return order;
    }

    @Override
    public OrderItem toEntity(OrderItemRequest request, Order order) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());
        item.setPrice(request.getPrice());
        return item;
    }

    @Override
    public Address toAddress(AddressRequest request, Order order, AddressType type) {
        Address a = new Address();
        a.setOrder(order);
        a.setType(type);
        a.setStreet(request.getStreet());
        a.setCity(request.getCity());
        a.setPostalCode(request.getPostalCode());
        a.setCountry(request.getCountry());
        return a;
    }

    @Override
    public OrderPayment toPayment(OrderRequest request, Order order) {
        BigDecimal subTotal = calculateSubTotal(request);
        BigDecimal shippingAmount = request.getShipping().getFee();
        BigDecimal taxAmount     = subTotal.multiply(new BigDecimal("0.21")); // VAT 21%
        BigDecimal discountAmount= BigDecimal.ZERO;
        BigDecimal totalAmount   = subTotal.add(shippingAmount).add(taxAmount).subtract(discountAmount);

        OrderPayment p = new OrderPayment();
        p.setOrder(order);
        p.setSubTotal(subTotal);
        p.setShippingAmount(shippingAmount);
        p.setTaxAmount(taxAmount);
        p.setDiscountAmount(discountAmount);
        p.setTotalAmount(totalAmount);
        p.setCurrency(request.getCurrency());
        p.setShippingMethod(request.getShipping().getMethod());
        p.setPaymentMethod(request.getPaymentMethod());
        p.setPaymentStatus(request.getPaymentStatus());
        return p;
    }

    @Override
    public OrderResponse toResponse(Order order,
                                    List<OrderItem> items,
                                    List<Address> addresses,
                                    OrderPayment payment) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .status(order.getStatus().name())

                .subTotal(payment.getSubTotal())
                .shippingAmount(payment.getShippingAmount())
                .taxAmount(payment.getTaxAmount())
                .discountAmount(payment.getDiscountAmount())
                .totalAmount(payment.getTotalAmount())
                .currency(payment.getCurrency())

                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus().name())
                .shippingMethod(payment.getShippingMethod())

                .contactEmail(order.getContactEmail())
                .contactPhone(order.getContactPhone())

                .items(mapItems(items))
                .billingAddress(mapAddress(addresses, AddressType.BILLING))
                .shippingAddress(mapAddress(addresses, AddressType.SHIPPING))

                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // --- helper methods below ---

    private static BigDecimal calculateSubTotal(OrderRequest request) {
        return request.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static List<OrderItemResponse> mapItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> new OrderItemResponse(item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()))
                .collect(Collectors.toList());
    }

    private static AddressResponse mapAddress(List<Address> addresses, AddressType type) {
        return addresses.stream()
                .filter(address -> address.getType() == type)
                .map(address -> new AddressResponse(address.getStreet(),
                        address.getCity(),
                        address.getPostalCode(),
                        address.getCountry()))
                .findFirst()
                .orElse(null);
    }
}
