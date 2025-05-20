package com.test.accesa.DTO;

public record ProductRecommendationDTO(String name,
                                       String brand,
                                       String storeName,
                                       double price,
                                       double packageQuantity,
                                       String packageType,
                                       double pricePerUnit,
                                       double discountPercentage) {
}
