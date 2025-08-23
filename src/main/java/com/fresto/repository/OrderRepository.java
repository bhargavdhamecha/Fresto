package com.fresto.repository;


import com.fresto.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * OrderRepository is the interface for accessing Order data from the database.
 */
@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
}
