package com.lumastyle.product.controller;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "CRUD operations for products")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(summary = "Creates a new product with the provided data.")
    @ApiResponse(responseCode = "201", description = "Product successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Received request to create product: {}", request.getName());
        return service.createProduct(request);
    }

    @Operation(summary = "Returns a single product by its unique identifier (ID)")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        log.info("Received request to get product with ID: {}", id);
        return service.getProductById(id);
    }

    @Operation(summary = "List all products", description = "Returns a paginated list of all products.")
    @ApiResponse(responseCode = "200", description = "Products retrieved")
    @GetMapping
    public Page<ProductResponse> getAllProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Received request to list products - page: {}, size: {}", page, size);
        return service.getAllProducts(page, size);
    }

    @Operation(summary = "Search products", description = "Search products by text query in name, description, or tags.")
    @ApiResponse(responseCode = "200", description = "Search results retrieved")
    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Received search request - query: '{}', page: {}, size: {}", query, page, size);
        return service.searchProducts(query, page, size);
    }

    @Operation(summary = "Update an existing product", description = "Updates an existing product identified by ID.")
    @ApiResponse(responseCode = "200", description = "Product successfully updated")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request
    ) {
        log.info("Updating product with ID {}", id);
        return service.updateProduct(id, request);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its unique identifier (ID).")
    @ApiResponse(responseCode = "204", description = "Product successfully deleted")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product with ID: {}", id);
        service.deleteProduct(id);
    }
}
