package com.test.accesa.controller;

import com.test.accesa.DTO.PricePointDTO;
import com.test.accesa.DTO.ProductRecommendationDTO;
import com.test.accesa.entity.Product;
import com.test.accesa.repository.ProductRepository;
import com.test.accesa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/price-history")
    public List<PricePointDTO> getPriceHistory(
            @RequestParam String name,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category) {
        return productService.getPriceHistory(name, storeName, brand, category);
    }


    /**
     * Simple CRUD endpoints
     */
    @GetMapping("/getAll")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/getById/{id}")
    public Product getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/addProduct")
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }
}
