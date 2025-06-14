package com.lumastyle.order.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String status;

    private BigDecimal subTotal;
    private BigDecimal shippingAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currency;

    private String paymentMethod;
    private String paymentStatus;
    private String shippingMethod;

    private List<OrderItemResponse> items;
    private AddressResponse billingAddress;
    private AddressResponse shippingAddress;

    private String contactEmail;
    private String contactPhone;

    private Instant createdAt;
    private Instant updatedAt;
}
