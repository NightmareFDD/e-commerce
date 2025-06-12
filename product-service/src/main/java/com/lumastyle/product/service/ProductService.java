package com.lumastyle.product.service;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(int page, int size);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    Page<ProductResponse> searchProducts(String query, int page, int size);
}
