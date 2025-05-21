package com.test.accesa.DTO;

public record PriceAlertRequest(long userId, long productId, double targetPrice) {
    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }
}
