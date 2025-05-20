package com.test.accesa.DTO;

import java.util.List;
import java.util.Map;

public record OptimalCartDTO(Map<String, List<BestItemDTO>> storeToItems,
                             double totalCost) {
}
