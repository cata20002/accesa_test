package com.test.accesa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "category")
    private String category;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "package_quantity")
    private double packageQuantity;

    @Column(name = "package_type")
    private String packageType;

    @Column(name = "price")
    private double price;

    @Column(name = "currency")
    private String currency;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Discount> discounts = new ArrayList<>();

    @Transient
    public double getPricePerUnit() {
        return price / packageQuantity;
    }
}
