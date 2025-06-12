package com.lumastyle.product.mapper;

import com.lumastyle.product.dto.ProductRequest;
import com.lumastyle.product.dto.ProductResponse;
import com.lumastyle.product.entity.Brand;
import com.lumastyle.product.entity.Category;
import com.lumastyle.product.entity.Product;
import com.lumastyle.product.repository.BrandRepository;
import com.lumastyle.product.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    // === Helper methods ===

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
    }

    private Brand findBrand(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found: " + brandId));
    }

}
