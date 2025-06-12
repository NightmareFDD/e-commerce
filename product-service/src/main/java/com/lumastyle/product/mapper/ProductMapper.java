package com.lumastyle.product.mapper;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.entity.Product;

public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);

}