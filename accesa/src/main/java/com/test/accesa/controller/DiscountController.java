package com.test.accesa.controller;

import com.test.accesa.DTO.OptimalCartDTO;
import com.test.accesa.DTO.ShoppingItemDTO;
import com.test.accesa.DTO.TopDiscountDTO;
import com.test.accesa.entity.Discount;
import com.test.accesa.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("/top5")
    public Map<String, List<TopDiscountDTO>> getTop5DiscountsPerStore() {
        return discountService.getTop5DiscountsPerStore();
    }

    //could have a DTO, but since it is not used actively, it should be fine
    @GetMapping("/recent")
    public List<Discount> getAllRecentDiscounts() {
        return discountService.getAllRecentDiscounts();
    }

    @PostMapping("/optimize")
    public OptimalCartDTO optimizeBasket(@RequestBody List<ShoppingItemDTO> basket) {
        return discountService.optimizeShoppingBasket(basket);
    }

    /**
     * Simple CRUD endpoints
     */
    @GetMapping("/getAll")
    public List<Discount> getAllDiscounts() {
        return discountService.getAllDiscounts();
    }

    @GetMapping("/getById/{id}")
    public Discount getDiscountById(@PathVariable long id) {
        return discountService.getDiscountById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDiscount(@PathVariable long id) {
        discountService.deleteDiscountById(id);
    }

    @PostMapping("/addDiscount")
    public Discount saveDiscount(@RequestBody Discount discount) {
        return discountService.saveDiscount(discount);
    }

}
