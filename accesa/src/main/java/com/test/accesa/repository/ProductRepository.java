package com.test.accesa.repository;

import com.test.accesa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);

    List<Product> findByNameContainingIgnoreCaseAndBrandIgnoreCase(String s, String brand);

    List<Product> findByCategoryIgnoreCase(String category);
}
