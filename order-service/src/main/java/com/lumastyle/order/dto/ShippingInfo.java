package com.lumastyle.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingInfo {
    @NotBlank
    private String method;

    @NotNull
    private BigDecimal fee;
}
