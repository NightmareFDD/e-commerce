package com.lumastyle.product.mapper;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.entity.Product;
import com.lumastyle.product.event.ProductEvent;
import com.lumastyle.product.infrastructure.search.ProductDocument;

/**
 * Interface defining mapping methods between DTOs, JPA entity,
 * Elasticsearch document and Kafka event.
 */
public interface ProductMapper {

    /**
     * Map incoming REST DTO to JPA entity.
     */
    Product toEntity(ProductRequest request);

    /**
     * Update the existing JPA entity from incoming DTO.
     */
    void updateEntity(ProductRequest request, Product existing);

    /**
     * Map JPA entity to REST API response DTO.
     */
    ProductResponse toResponse(Product product);

    /**
     * Map JPA entity to Elasticsearch document.
     */
    ProductDocument toDocument(Product product);

    /**
     * Map JPA entity to Kafka event payload.
     */
    ProductEvent toEvent(Product product);

    /**
     * Maps an Elasticsearch document representation of a product into a REST API response DTO.
     */
    ProductResponse toResponseFromDocument(ProductDocument productDocument);
}