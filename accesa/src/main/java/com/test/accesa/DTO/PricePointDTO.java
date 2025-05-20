package com.test.accesa.DTO;

import java.util.Date;

/**
 * A Data Transfer Object (DTO) representing a price point.
 * We only need the date and the corresponding price for a simple plot.
 *
 * @param date  The date of the price point.
 * @param price The price at the given date.
 */
public record PricePointDTO(Date date, double price) {
}
