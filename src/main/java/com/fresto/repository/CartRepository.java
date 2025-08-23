package com.fresto.repository;

import com.fresto.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query(value = "SELECT * FROM cart_master WHERE user_id = :id", nativeQuery = true)
    Optional<Cart> findByUserId(Integer id);
}
