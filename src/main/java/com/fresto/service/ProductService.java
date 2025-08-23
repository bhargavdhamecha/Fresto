package com.fresto.service;

import com.fresto.constant.ProductCategory;
import com.fresto.dto.ProductRequestDTO;
import com.fresto.entity.Product;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.repository.ProductRepository;
import com.fresto.utils.DtoEntityMapperUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fresto.constant.ProductCategory.MEN_CLOTHING;
import static com.fresto.constant.ProductCategory.WOMEN_CLOTHING;

/**
 * Service class for managing products.
 */
@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Add methods for product management here, such as:
    // - addProduct
    // - updateProduct
    // - deleteProduct
    // - getProductById
    // - getAllProducts

    public List<Product> getAllProducts() {
        log.info("Fetching all products from the db");
        return productRepository.findAll();
    }

    public Product getProductDetailsById(int productId) throws ProductNotFoundException {
        log.info("Fetching product with ID: {}", productId);
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public boolean addProduct(ProductRequestDTO requestDTO) {
        log.info("Adding new product with details: {}", requestDTO);
        Product p = productRepository.updateOrInsert(DtoEntityMapperUtility.requestDtoToProductEntity(requestDTO));
        log.info("Product added successfully: {}", p);
        return true;
    }

    public boolean updateProduct(ProductRequestDTO requestDTO, Integer productId) throws ProductNotFoundException {
        log.debug("Updating product with requestVO: {}", requestDTO);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        product.setProductName(requestDTO.getName());
        product.setProductDesc(requestDTO.getDescription());
        product.setProductPrice(requestDTO.getPrice());
        product.setProductImage(requestDTO.getImageUrl());
        product.setProductCategory(requestDTO.getCategory());
        product.setProductQuantity(requestDTO.getQuantity());
        productRepository.updateOrInsert(product);
        log.debug("Product updated successfully: {}", product);
        return true;
    }

    public boolean updateProduct(Product product, Integer productId) throws ProductNotFoundException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductDesc(product.getProductDesc());
        existingProduct.setProductPrice(product.getProductPrice());
        existingProduct.setProductImage(product.getProductImage());
        existingProduct.setProductCategory(product.getProductCategory());
        existingProduct.setProductQuantity(product.getProductQuantity());
        productRepository.updateOrInsert(existingProduct);
        log.debug("Product updated successfully: {}", existingProduct);
        return true;
    }

    public List<Product> findAllLowStockProducts() {
        return productRepository.findAllLowStockProducts(5);
    }

    public boolean deleteProduct(Integer id) {
        if (productRepository.existsById(id)) {
            log.info("Deleting product with ID: {}", id);
            productRepository.deleteById(id);
            log.debug("Product deleted successfully with ID: {}", id);
            return true;
        } else {
            log.error("Product not found with id: {}", id);
            return false;
        }
    }

    public boolean checkProductAvailability(Product product, int orderQuantity) {
        log.info("Checking availability for productId: {} availableQuantity: {}, requested quantity: {}", product.getProductId(), product.getProductQuantity(), orderQuantity);
        return (product.getProductQuantity() - orderQuantity) < 0;
    }

    public List<Product> getAllProductsByCategory(String category) {
        log.info("Fetching all products by category: {}", category);
        return productRepository.findAllByCategory(category.toUpperCase());
    }

    public List<Product> searchProductsByNameOrDescription(String query) {
        log.info("Searching products with query: {}", query);
        return productRepository.searchProductsByNameOrDescription(query);
    }
}
