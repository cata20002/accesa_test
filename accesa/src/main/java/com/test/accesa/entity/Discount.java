package com.test.accesa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name= "product_name")
    private String productName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "package_quantity")
    private double packageQuantity;

    @Column(name = "package_type")
    private String packageType;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "discount_percentage")
    private double discountPercentage;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

}
