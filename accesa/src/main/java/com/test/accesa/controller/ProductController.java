package com.test.accesa.controller;

import com.test.accesa.DTO.ProductRecommendationDTO;
import com.test.accesa.entity.Product;
import com.test.accesa.repository.ProductRepository;
import com.test.accesa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/substitutes/{productId}")
    public List<ProductRecommendationDTO> getSubstitutes(@PathVariable Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productService.getCheaperAlternatives(product);
    }
}
