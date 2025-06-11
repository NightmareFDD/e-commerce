package com.lumastyle.product.service.impl;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.entity.Product;
import com.lumastyle.product.exception.NotFoundException;
import com.lumastyle.product.mapper.ProductMapper;
import com.lumastyle.product.repository.ProductRepository;
import com.lumastyle.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final String NOT_FOUND_MSG = "Product not found with ID: ";

    private final ProductRepository repository;
    private final ProductMapper mapper;

    // Create: evict both detail and list caches
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "PRODUCT_CACHE", allEntries = true),
            @CacheEvict(cacheNames = "PRODUCT_LIST_CACHE", key = "'all'")
    })
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());
        Product product = mapper.toEntity(request);
        Product saved = repository.save(product);
        log.info("Product created: id: {}, name: {}", saved.getId(), saved.getName());
        return mapper.toResponse(saved);
    }

    // Cache detail by ID
    @Override
    @Cacheable(cacheNames = "PRODUCT_CACHE", key = "#id")
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MSG + id));
        return mapper.toResponse(product);
    }

    // Cache the full product list under a fixed key
    @Override
    @Cacheable(cacheNames = "PRODUCT_LIST_CACHE", key = "'all'")
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // Update: update detail cache for this ID and evict list cache
    @Override
    @Caching(
            put = @CachePut(cacheNames = "PRODUCT_CACHE", key = "#result.id"),
            evict = @CacheEvict(cacheNames = "PRODUCT_LIST_CACHE", key = "'all'")
    )
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MSG + id));

        mapper.updateEntityFromRequest(request, existingProduct);
        Product saved = repository.save(existingProduct);
        log.info("Product with ID {} was successfully updated: name={}, price={}", saved.getId(), saved.getName(), saved.getPrice());
        return mapper.toResponse(saved);
    }

    // Delete: evict this ID and list cache
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "PRODUCT_CACHE", key = "#id"),
            @CacheEvict(cacheNames = "PRODUCT_LIST_CACHE", key = "'all'")
    })
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MSG + id));
        repository.delete(entity);
        log.info("Product deleted with ID: {}", id);
    }
}
