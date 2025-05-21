package com.test.accesa.service;

import com.test.accesa.entity.Discount;
import com.test.accesa.entity.Product;
import com.test.accesa.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ParsingService {

    public static List<Product> parseCsvToProducts(String filePath) {
        List<Product> products = new ArrayList<>();
        String fileName = new java.io.File(filePath).getName();

        String[] parts = fileName.split("_|\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid file name format. Expected format: store_yyyy-MM-dd.csv");
        }

        String storeName = parts[0];
        Date createdAt;
        try {
            createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date in file name: " + parts[1]);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(";");
                if (tokens.length < 8) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                Product product = new Product();
                product.setProductCode(tokens[0].trim());
                product.setName(tokens[1].trim());
                product.setCategory(tokens[2].trim());
                product.setBrand(tokens[3].trim());

                try {
                    product.setPackageQuantity(Double.parseDouble(tokens[4]));
                    product.setPackageType(tokens[5].trim());
                    product.setPrice(Double.parseDouble(tokens[6]));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line due to number format error: " + line);
                    continue;
                }

                product.setCurrency(tokens[7].trim());
                product.setCreatedAt(createdAt);
                product.setStoreName(storeName.trim());

                products.add(product);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static List<Discount> parseDiscounts(String filePath, ProductRepository productRepository) {
        List<Discount> discounts = new ArrayList<>();
        String fileName = new java.io.File(filePath).getName();

        String[] parts = fileName.split("_");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Filename must be in format: store_discounts_yyyy-MM-dd.csv");
        }
        String storeName = parts[0];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(";");

                String productCode = tokens[0].trim();
                Optional<Product> productOpt = productRepository.findAll()
                        .stream()
                        .filter(p -> p.getName().equalsIgnoreCase(tokens[1].trim()) &&
                                p.getBrand().equalsIgnoreCase(tokens[2].trim()))
                        .findFirst();
                List<Product> products = productRepository.findAll();
                for (Product product : products) {
                    System.out.println(product);
                }
                System.out.println(tokens[1].trim() + " " + tokens[2].trim());

                if (productOpt.isEmpty()) {
                    System.err.println("No matching product found for discount: " + line);
                    continue;
                }

                Product product = productOpt.get();

                try {
                    Discount discount = new Discount();
                    discount.setProduct(product);
                    discount.setProductName(tokens[1].trim());
                    discount.setBrand(tokens[2].trim());
                    discount.setPackageQuantity(Double.parseDouble(tokens[3].trim()));
                    discount.setPackageType(tokens[4].trim());
                    discount.setProductCategory(tokens[5].trim());
                    discount.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(tokens[6].trim()));
                    discount.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(tokens[7].trim()));
                    discount.setDiscountPercentage(Double.parseDouble(tokens[8].trim()));
                    discount.setStoreName(storeName);

                    discounts.add(discount);
                } catch (Exception e) {
                    System.err.println("Error parsing discount line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return discounts;
    }
}
