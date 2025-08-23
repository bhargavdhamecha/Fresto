package com.fresto.entity;

import com.fresto.constant.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Product entity represents a product in the e-commerce application.
 * It contains details such as product ID, name, price, description, and image URL.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "product_master", uniqueConstraints = {@UniqueConstraint(name = "unique_product", columnNames = {"productName"})})
public class Product {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @NotNull
    private String productName;

    private double productPrice;

    @Column(columnDefinition = "TEXT")
    private String productDesc;

    private String productImage;

    private int productQuantity;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    public Product(String pName, double pPrice, String pDescription, String pImageUrl, int pQuantity) {
        this.productName = pName;
        this.productPrice = pPrice;
        this.productDesc = pDescription;
        this.productImage = pImageUrl;
        this.productQuantity = pQuantity;
    }
}
