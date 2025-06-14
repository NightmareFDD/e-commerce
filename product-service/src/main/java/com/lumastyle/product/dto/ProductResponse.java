package com.lumastyle.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object representing product details")
public class ProductResponse {
    @Schema(description = "Unique identifier of the product", example = "1001")
    private Long id;
    @Schema(description = "Product name", example = "Red T-Shirt")
    private String name;
    @Schema(description = "Short product description", example = "Comfortable cotton t-shirt in red color")
    private String description;
    @Schema(description = "Product price", example = "19.99")
    private BigDecimal price;
    @Schema(description = "Whether the product is available", example = "true")
    private Boolean available;
    @Schema(description = "Unique identifier of the category", example = "1")
    private Long categoryId;
    @Schema(description = "Category name of the product", example = "Clothing")
    private String categoryName;
    @Schema(description = "Unique identifier of the brand", example = "101")
    private Long brandId;
    @Schema(description = "Brand name of the product", example = "Nike")
    private String brandName;
    @Schema(description = "Tags associated with the product", example = "[\"summer\", \"sale\"]")
    private String tags;
}
