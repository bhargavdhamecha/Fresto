package com.fresto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * OrderItem entity represents an item in an order in the e-commerce application.
 * It contains details such as order item ID, order ID, product ID, quantity, and price.
 */
@ToString
@Getter
@Setter
@Table(name = "order_item_master")
@Entity
@NoArgsConstructor
public class OrderItem {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    private Orders order;

    private int quantity;


    private double price;
}
