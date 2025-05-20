package com.test.accesa.repository;

import com.test.accesa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);

    List<Product> findByNameContainingIgnoreCaseAndBrandIgnoreCase(String s, String brand);

    List<Product> findByCategoryIgnoreCase(String category);

    /**
     * Get all products by same name, store name, brand or category
     * Used for price history (Feature 4)
     * @param name: product name
     * @param storeName: store name
     * @param brand: product brand
     * @param category: product category
     * @return: list of products matching the criteria
     */
    @Query("SELECT p FROM Product p WHERE p.name = :name"
            + " AND (:storeName IS NULL OR p.storeName = :storeName)"
            + " AND (:brand IS NULL OR p.brand = :brand)"
            + " AND (:category IS NULL OR p.category = :category)"
            + " ORDER BY p.createdAt ASC")
    List<Product> getPriceHistory(
            @Param("name") String name,
            @Param("storeName") String storeName,
            @Param("brand") String brand,
            @Param("category") String category
    );
}
