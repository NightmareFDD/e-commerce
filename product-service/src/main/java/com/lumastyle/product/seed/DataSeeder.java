package com.lumastyle.product.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumastyle.product.entity.Brand;
import com.lumastyle.product.entity.Category;
import com.lumastyle.product.repository.BrandRepository;
import com.lumastyle.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("=== STARTING DATA SEEDER ===");
        seedBrands();
        seedCategories();
        log.info("=== DATA SEEDER FINISHED ===");
    }

    private void seedBrands() throws Exception{
        InputStream input = getClass()
                .getResourceAsStream("/data/brands.json");
        List<Brand> brands = objectMapper.readValue(input, new TypeReference<>() {
        });
        for (Brand brand : brands) {
            String name = brand.getName().trim();
            if (!brandRepository.existsByNameIgnoreCase(name)) {
                brandRepository.save(new Brand(null, name));
            }
        }
    }

    private void seedCategories() throws Exception{
        InputStream input = getClass()
                .getResourceAsStream("/data/category.json");
        List<Category> categories = objectMapper.readValue(input, new TypeReference<>() {
        });
        for (Category category : categories) {
            String name = category.getName().trim();
            if (!categoryRepository.existsByNameIgnoreCase(name)) {
                categoryRepository.save(new Category(null, name));
            }
        }
    }
}
