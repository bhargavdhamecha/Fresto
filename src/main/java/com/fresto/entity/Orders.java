package com.fresto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Orders entity represents an order in the e-commerce application.
 * It contains details such as order ID, name, price, quantity, date, total amount, and associated user.
 */
@Getter
@Setter
@Entity
@Table(name = "order_master")
public class Orders {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private LocalDateTime orderDate;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Override
    public String toString() {
        return "Orders [orderId=" + orderId +
                ", orderDate=" + orderDate + ", totalAmount=" + totalAmount +
                ", user=" + user.getUserName() + " userId: " + user.getUserId() + "]";
    }
}
