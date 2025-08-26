package com.fresto.repository;

import com.fresto.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Product entities.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Transactional
    default Product updateOrInsert(Product entity) {
        return save(entity);
    }

    @Query(value = "SELECT * FROM product_master WHERE product_quantity < :quantity", nativeQuery = true)
    List<Product> findAllLowStockProducts(@Param("quantity") int quantity);

    @Query(value = "SELECT * FROM product_master WHERE product_category = :category", nativeQuery = true)
    List<Product> findAllByCategory(@Param("category") String category);

    @Query(value = "SELECT * FROM product_master WHERE product_name LIKE %:query% OR product_desc LIKE %:query%", nativeQuery = true)
    List<Product> searchProductsByNameOrDescription(String query);

    List<Product> findAllByOrderByProductNameAsc();

    List<Product> findAllByOrderByProductPriceAsc();
}
