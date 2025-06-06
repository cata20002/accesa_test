package com.test.accesa.service;

import com.test.accesa.DTO.BestItemDTO;
import com.test.accesa.DTO.OptimalCartDTO;
import com.test.accesa.DTO.ShoppingItemDTO;
import com.test.accesa.DTO.TopDiscountDTO;
import com.test.accesa.entity.Discount;
import com.test.accesa.entity.Product;
import com.test.accesa.repository.DiscountRepository;
import com.test.accesa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Feature 3:
     * This method returns all discounts that are active today or were active yesterday.
     * The result is a list of Discount objects, wrapped in a ShoppingItemDTO, to avoid circular dependencies.
     *
     * @return List<ShoppingItemDTO> - A list of discounts that are active today or were active yesterday.
     */
    public List<ShoppingItemDTO> getAllRecentDiscounts() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        Date today = new Date();
        List<Discount> discountedItems = discountRepository.findAllByStartDateBetween(yesterday, today);
        List<Product> discountedProducts = new ArrayList<>();
        for (Discount d : discountedItems) {
            discountedProducts.add(d.getProduct());
        }
        return discountedProducts.stream()
                .map(p -> new ShoppingItemDTO(p.getName(), p.getBrand(), p.getPackageQuantity(), p.getPackageType()))
                .collect(Collectors.toList());
    }

    /**
     * Feature 2:
     * This method returns the top 5 discounts per store, sorted by discount percentage in descending order.
     * The result is a map where the key is the store name and the value is a list of TopDiscountDTO objects.
     * Each TopDiscountDTO object contains information about the product, brand, discount percentage, store name,
     * start date, and end date of the discount.
     *
     * @return Map<String, List < TopDiscountDTO>> - A map containing the top 5 discounts per store.
     */
    public Map<String, List<TopDiscountDTO>> getTop5DiscountsPerStore() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        List<Discount> allDiscounts = discountRepository.findAll();

        return allDiscounts.stream()
                //could maybe add a filtering by date to only show active discounts
                .collect(Collectors.groupingBy(
                        Discount::getStoreName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                discounts -> discounts.stream()
                                        .sorted(Comparator.comparingDouble(Discount::getDiscountPercentage).reversed())
                                        .limit(5)
                                        .map(d -> new TopDiscountDTO(
                                                d.getProductName(),
                                                d.getBrand(),
                                                d.getDiscountPercentage(),
                                                d.getStoreName(),
                                                df.format(d.getStartDate()),
                                                df.format(d.getEndDate())
                                        ))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    /**
     * Feature 1:
     * This method optimizes a shopping basket by finding the best price for each item in the basket.
     * It returns an OptimalCartDTO object containing the optimized shopping cart, by checking all products with the same name and brand and updates the total cost.
     * The shopping list is sent as a normal list, not a standalone custom object.
     * The optimalCartDTO contains a map of store names to lists of BestItemDTO objects, which represent the best price for each item in the basket.
     *
     * @param basket - A list of ShoppingItemDTO objects representing the items in the shopping basket.
     * @return OptimalCartDTO - An object containing the optimized shopping cart and total cost.
     */
    public OptimalCartDTO optimizeShoppingBasket(List<ShoppingItemDTO> basket) {
        Date today = new Date();
        Map<String, List<BestItemDTO>> storeMap = new HashMap<>();
        double totalCost = 0.0;

        for (ShoppingItemDTO item : basket) {
            List<Product> matches = productRepository.findByNameContainingIgnoreCaseAndBrandIgnoreCase(
                    item.productName(), item.brand()
            );

            if (matches.isEmpty()) continue;

            BestItemDTO best = null;

            for (Product p : matches) {
                double basePrice = p.getPrice();
                Optional<Discount> discount = p.getDiscounts().stream()
                        .filter(d -> !today.before(d.getStartDate()) && !today.after(d.getEndDate()))
                        .findFirst();

                double discountPercent = discount.map(Discount::getDiscountPercentage).orElse(0.0);
                double priceWithDiscount = basePrice * (1 - discountPercent / 100);
                double totalItemPrice = priceWithDiscount * item.quantity();

                BestItemDTO optimized = new BestItemDTO(
                        p.getName(), p.getBrand(), p.getStoreName(),
                        item.quantity(), p.getPackageType(),
                        priceWithDiscount, totalItemPrice, discountPercent
                );

                if (best == null || optimized.totalPrice() < best.totalPrice()) {
                    best = optimized;
                }
            }

            storeMap.computeIfAbsent(best.storeName(), k -> new ArrayList<>()).add(best);
            totalCost += best.totalPrice();
        }

        return new OptimalCartDTO(storeMap, totalCost);
    }

    /**
     * Simple CRUD operations for further use
     */
    public void deleteDiscountById(Long id) {
        discountRepository.deleteById(id);
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id).orElse(null);
    }

    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }
}
