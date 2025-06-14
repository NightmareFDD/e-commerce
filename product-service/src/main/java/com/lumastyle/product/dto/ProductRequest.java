package com.lumastyle.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request object for creating or updating a product")
public class ProductRequest {

    @NotBlank(message = "Product name must not be blank")
    @Schema(description = "Product name", example = "Red T-Shirt")
    private String name;

    @Size(max = 255, message = "Description must be at most 255 characters")
    @Schema(description = "Short product description", example = "Comfortable cotton t-shirt in red color")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "19.99")
    private BigDecimal price;

    @NotNull
    @Schema(description = "Whether the product is available", example = "true")
    private Boolean available;

    @NotNull(message = "Category id must not be blank")
    @Schema(description = "Category ID the product belongs to", example = "1")
    private Long categoryId;

    @NotNull(message = "Brand id must not be blank")
    @Schema(description = "Brand ID of the product", example = "1")
    private Long brandId;

    @Schema(description = "Optional tags for product search and filtering", example = "[\"summer\", \"sale\"]")
    private String tags;
}
