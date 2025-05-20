package com.test.accesa.DTO;

public record TopDiscountDTO(String productName,
                             String brand,
                             double discountPercentage,
                             String storeName,
                             String startDate,
                             String endDate
) {
}

