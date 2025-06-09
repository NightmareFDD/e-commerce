package com.lumastyle.product.repository;

import com.lumastyle.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long>{
}
