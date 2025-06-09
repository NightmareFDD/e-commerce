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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()") // use also for update method
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());
        Product product = mapper.toEntity(request);
        return mapper.toResponse(repository.save(product));
    }

    @Override
    @Cacheable(value = "PRODUCT_CACHE", key = "#id")
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
        return mapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()")
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

        mapper.updateEntityFromRequest(request, product);

        return mapper.toResponse(repository.save(product));
    }

    @Override
    @CacheEvict(value = "PRODUCT_CACHE", key = "#id")
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Product to delete not found: {}", id);
            throw new NotFoundException("Product not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
