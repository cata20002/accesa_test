package com.test.accesa.service;

import com.test.accesa.DTO.PriceAlertRequest;
import com.test.accesa.entity.PriceAlert;
import com.test.accesa.entity.Product;
import com.test.accesa.entity.User;
import com.test.accesa.repository.PriceAlertRepository;
import com.test.accesa.repository.ProductRepository;
import com.test.accesa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceAlertService {

    @Autowired
    private PriceAlertRepository priceAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public void savePriceAlert(PriceAlert priceAlert) {
        priceAlertRepository.save(priceAlert);
    }

    public boolean createAlertRequest(PriceAlertRequest req) {
        Optional<User> userOpt = userRepository.findById(req.getUserId());
        Optional<Product> productOpt = productRepository.findById(req.getProductId());

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return false;
        }

        PriceAlert alert = new PriceAlert();
        alert.setUser(userOpt.get());
        alert.setProduct(productOpt.get());
        alert.setTargetPrice(req.getTargetPrice());
        priceAlertRepository.save(alert);
        return true;
    }

    /**
     * Feature 6: Check for price alerts
     * This method checks for price alerts every 6 seconds, for demo purposes
     * This can be further optimized to check for the alerts of the logged-in user
     */
    @Scheduled(fixedRate = 6000)
    public void checkAlerts() {
        List<PriceAlert> alerts = priceAlertRepository.findAllByisActiveFalse();

        for (PriceAlert alert : alerts) {
            Product product = alert.getProduct();
            if (product.getPrice() <= alert.getTargetPrice()) {
                System.out.println("ALERT: Price drop for " + product.getName());

                alert.setActive(true);
                priceAlertRepository.save(alert);
            }
        }
    }
}
