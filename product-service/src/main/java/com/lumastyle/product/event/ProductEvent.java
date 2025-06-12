package com.lumastyle.product.event;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payload for Kafka events related to Product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEvent {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
    private Long categoryId;
    private Long brandId;
    private List<String> tags;
}
