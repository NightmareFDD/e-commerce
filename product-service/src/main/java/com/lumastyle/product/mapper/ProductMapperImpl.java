package com.lumastyle.product.mapper;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.entity.Brand;
import com.lumastyle.product.entity.Category;
import com.lumastyle.product.entity.Product;
import com.lumastyle.product.event.ProductEvent;
import com.lumastyle.product.infrastructure.search.ProductDocument;
import com.lumastyle.product.repository.BrandRepository;
import com.lumastyle.product.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements ProductMapper {

    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .available(request.getAvailable())
                .category(findCategory(request.getCategoryId()))
                .brand(findBrand(request.getBrandId()))
                .tags(request.getTags())
                .build();
    }

    @Override
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .available(product.getAvailable())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .tags(product.getTags())
                .build();
    }

    @Override
    public void updateEntity(ProductRequest request, Product existing) {
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setAvailable(request.getAvailable());
        existing.setTags(request.getTags());
        existing.setCategory(findCategory(request.getCategoryId()));
        existing.setBrand(findBrand(request.getBrandId()));
    }

    @Override
    public ProductDocument toDocument(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .available(product.getAvailable())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .tags(product.getTags())
                .build();
    }

    @Override
    public ProductEvent toEvent(Product product) {
        List<String> tagList = getTagList(product);

        return ProductEvent.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .available(product.getAvailable())
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
                .tags(tagList)
                .build();
    }

    @Override
    public ProductResponse toResponseFromDocument(ProductDocument productDocument) {
        return ProductResponse.builder()
                .id(productDocument.getId())
                .name(productDocument.getName())
                .description(productDocument.getDescription())
                .price(productDocument.getPrice())
                .available(productDocument.getAvailable())
                .categoryId(productDocument.getCategoryId())
                .categoryName(productDocument.getCategoryName())
                .brandId(productDocument.getBrandId())
                .brandName(productDocument.getBrandName())
                .tags(productDocument.getTags())
                .build();
    }


    // === Helper methods ===

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
    }

    private Brand findBrand(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found: " + brandId));
    }

    private static List<String> getTagList(Product product) {
        String tags = product.getTags();
        if (!StringUtils.hasText(tags)) {
            return Collections.emptyList();
        }
        return Arrays.asList(StringUtils.tokenizeToStringArray(tags, ","));
    }

}
