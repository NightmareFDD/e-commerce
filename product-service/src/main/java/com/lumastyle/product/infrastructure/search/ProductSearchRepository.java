package com.lumastyle.product.infrastructure.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Perform full-text search on name, description, and tags fields.
 */
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
    Page<ProductDocument> findByNameContainingOrDescriptionContainingOrTagsContaining(
            String name, String description, String tags, Pageable pageable);
}
