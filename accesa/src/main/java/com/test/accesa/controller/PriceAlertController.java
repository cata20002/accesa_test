package com.test.accesa.controller;

import com.test.accesa.DTO.PriceAlertRequest;
import com.test.accesa.service.PriceAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/price-alerts")
public class PriceAlertController {

    @Autowired
    private PriceAlertService priceAlertService;


    @PostMapping("/add")
    public ResponseEntity<String> createAlert(@RequestBody PriceAlertRequest req) {
        boolean status = priceAlertService.createAlertRequest(req);

        if (!status) {
            return ResponseEntity.badRequest().body("Failed to create alert");
        }
        return ResponseEntity.ok("Alert created");
    }
}
