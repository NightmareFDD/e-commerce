package com.lumastyle.product.service.impl;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.entity.Product;
import com.lumastyle.product.event.ProductEvent;
import com.lumastyle.product.infrastructure.search.ProductDocument;
import com.lumastyle.product.infrastructure.search.ProductSearchRepository;
import com.lumastyle.product.mapper.ProductMapper;
import com.lumastyle.product.repository.ProductRepository;
import com.lumastyle.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {


    private static final String CACHE_PRODUCT = "PRODUCT_CACHE";
    private static final String CACHE_LIST = "PRODUCT_LIST_CACHE";
    private static final String PRODUCT_SEARCH_CACHE = "PRODUCT_SEARCH_CACHE";

    private static final String NOT_FOUND_MSG = "Product not found with ID: ";

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductSearchRepository search;
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_LIST}, allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: {}", request.getName());
        Product product = mapper.toEntity(request);
        Product saved = repository.save(product);

        // index saved entity in Elasticsearch
        ProductDocument document = mapper.toDocument(saved);
        search.save(document);

        // publish Kafka event after commit
        kafkaTemplate.send("product.created", mapper.toEvent(saved));

        log.info("Product created: id: {}, name: {}", saved.getId(), saved.getName());
        return mapper.toResponse(saved);
    }


    @Override
    @Cacheable(value = CACHE_PRODUCT, key = "#id")
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Retrieving product by id {}", id);
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MSG + id));
        return mapper.toResponse(product);
    }


    @Override
    @Cacheable(value = CACHE_LIST, key = "'page-'+#page+'-size-'+#size")
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page, int size) {
        log.info("Retrieving all products - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }


    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CACHE_PRODUCT, key = "#id"),
            @CacheEvict(value = CACHE_LIST, allEntries = true)
    })
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        Product existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MSG + id));

        mapper.updateEntity(request, existing);
        Product updated = repository.save(existing);

        // update index in Elasticsearch
        ProductDocument document = mapper.toDocument(updated);
        search.save(document);

        // publish Kafka event
        kafkaTemplate.send("product.updated", mapper.toEvent(updated));
        log.info("Product with ID {} was successfully updated: name: {}, price: {}",
                updated.getId(), updated.getName(), updated.getPrice()
        );
        return mapper.toResponse(updated);
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_PRODUCT, key = "#id"),
            @CacheEvict(value = CACHE_LIST, allEntries = true)
    })
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MSG + id));
        repository.deleteById(id);
        // remove from Elasticsearch index
        search.deleteById(id);

        // publish Kafka event
        kafkaTemplate.send("product.deleted", mapper.toEvent(entity));
        log.info("Product deleted with ID: {}", id);
    }

    @Override
    @Cacheable(value = PRODUCT_SEARCH_CACHE, key = "#query + '-' + #page + '-' + #size")
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String query, int page, int size) {
        log.info("Searching products with query: '{}', page: {}, size: {}", query, page, size);
        Pageable pageable = PageRequest.of(page, size);

        // Perform Elasticsearch full-text search via infrastructure adapter
        Page<ProductDocument> docs = search
                .findByNameContainingOrDescriptionContainingOrTagsContaining(query, query, query, pageable);

        // Map each ProductDocument to a ProductResponse DTO
        return docs.map(mapper::toResponseFromDocument);
    }
}
