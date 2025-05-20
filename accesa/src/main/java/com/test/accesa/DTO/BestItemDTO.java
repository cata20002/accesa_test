package com.test.accesa.DTO;

public record BestItemDTO(String productName,
                          String brand,
                          String storeName,
                          double quantity,
                          String unit,
                          double unitPrice,
                          double totalPrice,
                          double discountPercentage) {
}
