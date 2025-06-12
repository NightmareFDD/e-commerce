package com.lumastyle.product.controller;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Received request to create product: {}", request.getName());
        return service.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        log.info("Received request to get product with ID: {}", id);
        return service.getProductById(id);
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Received request to list products - page: {}, size: {}", page, size);
        return service.getAllProducts(page, size);
    }

    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Received search request - query: '{}', page: {}, size: {}", query, page, size);
        return service.searchProducts(query, page, size);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request
    ) {
        log.info("Updating product with ID {}", id);
        return service.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product with ID: {}", id);
        service.deleteProduct(id);
    }
}
