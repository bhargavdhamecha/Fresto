package com.fresto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * CartItem entity representing an item in a shopping cart.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "cart_item_master")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Orders order;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;


    private int quantity;

    private Double price;
}
