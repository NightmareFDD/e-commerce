package com.lumastyle.order.dto;

import com.lumastyle.order.entity.PaymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull
    private Long userId;

    @NotEmpty
    private List<OrderItemRequest> items;

    @NotNull
    private AddressRequest billingAddress;

    @NotNull
    private AddressRequest shippingAddress;

    @NotNull
    private ShippingInfo shipping;

    @NotBlank
    private String currency;

    @NotBlank
    private String paymentMethod;

    @NotNull
    private PaymentStatus paymentStatus;

    @Email
    @NotBlank
    private String contactEmail;

    @Pattern(regexp = "\\+?\\d{9,15}")
    private String contactPhone;
}
