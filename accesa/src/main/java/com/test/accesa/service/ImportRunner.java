package com.test.accesa.service;

import com.test.accesa.entity.Discount;
import com.test.accesa.entity.Product;
import com.test.accesa.repository.DiscountRepository;
import com.test.accesa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.test.accesa.service.ParsingService.parseCsvToProducts;
import static com.test.accesa.service.ParsingService.parseDiscounts;

@Component
@ConditionalOnProperty(name = "import.data.enabled", havingValue = "true", matchIfMissing = false)
public class ImportRunner implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public void run(String... args) {
        List<Product> profiProducts = parseCsvToProducts("src/main/resources/static/profi_2025-05-02.csv");
        for (Product product : profiProducts) {
            productRepository.save(product);
        }
        List<Product> lidlProducts = parseCsvToProducts("src/main/resources/static/lidl_2025-05-01.csv");
        for (Product product : lidlProducts) {
            productRepository.save(product);
        }
        List<Product> kauflandProducts = parseCsvToProducts("src/main/resources/static/kaufland_2025-05-11.csv");
        for (Product product : kauflandProducts) {
            productRepository.save(product);
        }
        List<Discount> lidlDiscounts = parseDiscounts("src/main/resources/static/lidl_discounts_2025-05-01.csv", productRepository);
        for (Discount discount : lidlDiscounts) {
            discountRepository.save(discount);
        }
        List<Discount> kauflandDiscounts = parseDiscounts("src/main/resources/static/kaufland_discounts_2025-05-11.csv", productRepository);
        for (Discount discount : kauflandDiscounts) {
            discountRepository.save(discount);
        }
        List<Discount> profiDiscounts = parseDiscounts("src/main/resources/static/profi_discounts_2025-05-02.csv", productRepository);
        for (Discount discount : profiDiscounts) {
            discountRepository.save(discount);
        }
    }
}
