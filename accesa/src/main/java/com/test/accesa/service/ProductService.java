package com.test.accesa.service;

import com.test.accesa.DTO.ProductRecommendationDTO;
import com.test.accesa.entity.Product;
import com.test.accesa.entity.Discount;
import com.test.accesa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

/**
     * Feature 5:
     * Finds cheaper alternatives for a given product based on its category and package type.
     * I opted to find alternatives only for 1 specific product, not a list with all alternatives, since it could be more useful in practice
     *
     * @param targetProduct The product for which to find alternatives.
     * @return A list of cheaper alternative products.
     */
    public List<ProductRecommendationDTO> getCheaperAlternatives(Product targetProduct) {
        List<Product> candidates = productRepository.findByCategoryIgnoreCase(targetProduct.getCategory());

        return candidates.stream()
                .filter(p -> p.getId() != targetProduct.getId())
                .filter(p -> p.getPackageType().equalsIgnoreCase(targetProduct.getPackageType()))
                .map(p -> {
                    double price = p.getPrice();
                    double discount = p.getDiscounts().stream()
                            .filter(d -> isTodayBetween(d.getStartDate(), d.getEndDate()))
                            .mapToDouble(Discount::getDiscountPercentage)
                            .findFirst().orElse(0.0);

                    double discountedPrice = price * (1 - discount / 100);
                    double pricePerUnit = discountedPrice / p.getPackageQuantity();

                    return new ProductRecommendationDTO(
                            p.getName(), p.getBrand(), p.getStoreName(),
                            discountedPrice, p.getPackageQuantity(), p.getPackageType(),
                            pricePerUnit, discount
                    );
                })
                .sorted(Comparator.comparingDouble(ProductRecommendationDTO::pricePerUnit))
                .limit(5)
                .collect(Collectors.toList());
    }

    private boolean isTodayBetween(Date start, Date end) {
        Date now = new Date();
        return !now.before(start) && !now.after(end);
    }
}
