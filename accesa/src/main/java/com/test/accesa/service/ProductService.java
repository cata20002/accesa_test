package com.test.accesa.service;

import com.test.accesa.DTO.PricePointDTO;
import com.test.accesa.DTO.ProductRecommendationDTO;
import com.test.accesa.entity.Discount;
import com.test.accesa.entity.Product;
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
     * The products should have the same package type, for the division to be relevant.
     * Only products from the same category are considered, to improve query speed, due to less results returned.
     * The alternatives are sorted by price per unit, and the top 5 are returned.
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

    /**
     * Feature 4:
     * Returns the price history of a product based on its name, store name, brand, and category.
     * The method retrieves all products matching the criteria using the custom query and maps them to a list of PricePointDTO objects.
     *
     * @param name      The name of the product.
     * @param storeName The name of the store.
     * @param brand     The brand of the product.
     * @param category  The category of the product.
     * @return A list of PricePointDTO objects representing the price history of the product.
     */
    public List<PricePointDTO> getPriceHistory(String name, String storeName, String brand, String category) {
        List<Product> products = productRepository.getPriceHistory(name, storeName, brand, category);
        return products.stream()
                .map(p -> new PricePointDTO(p.getCreatedAt(), p.getPrice()))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to check if the current date is between the start and end dates.
     *
     * @param start The start date.
     * @param end   The end date.
     * @return true if today is between start and end, false otherwise.
     */
    private boolean isTodayBetween(Date start, Date end) {
        Date now = new Date();
        return !now.before(start) && !now.after(end);
    }

    /**
     * Simple CRUD operations for the Product entity.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
