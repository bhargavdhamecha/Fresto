package com.fresto.service;

import com.fresto.constant.UserType;
import com.fresto.dto.ProductRequestDTO;
import com.fresto.dto.UserRequestDTO;
import com.fresto.entity.Orders;
import com.fresto.entity.Product;
import com.fresto.entity.User;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.exception.UserNotFoundException;
import com.fresto.utils.DtoEntityMapperUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service class for managing admin operations.
 */
@Slf4j
@Service
@Secured("ROLE_ADMIN")
public class AdminService {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public AdminService(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public List<User> getAllAdminUsers() {
        return userService.getAllByUserType(UserType.ADMIN);
    }

    public Long getAllCustomerUserCount() {
        return (long) userService.getAllByUserType(UserType.USER).size();
    }

    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    public List<Product> getLowStockProducts() {
        return productService.findAllLowStockProducts();
    }

    public boolean addUser(UserRequestDTO requestDTO) {
        return userService.registerUser(requestDTO);
    }

    public boolean updateUser(UserRequestDTO requestDTO, int userId) {
        log.debug("Enter --> updateUser with userId: {}", userId);
        return userService.updateUser(requestDTO, userId);
    }

    public boolean deleteUser(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userService.deleteUser(id);
    }

    public Map<String, Object> getUserDetailsById(Integer id) throws IllegalArgumentException, UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        HashMap<String, Object> responseObj = new HashMap<>();
        responseObj.put("user", userService.getUserById(id));
        responseObj.put("requestDTO", DtoEntityMapperUtility.userEntityToUserRequestDto((User) responseObj.get("user")));
        return responseObj;
    }

    public Map<String, Object> getProductDetailsById(Integer id) throws IllegalArgumentException, ProductNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        HashMap<String, Object> responseObj = new HashMap<>();
        responseObj.put("product", productService.getProductDetailsById(id));
        responseObj.put("requestDTO", DtoEntityMapperUtility.productEntityToProductRequestDto((Product) responseObj.get("product")));
        return responseObj;
    }

    public boolean addProduct(ProductRequestDTO product) {
        return productService.addProduct(product);
    }

    public boolean updateProduct(ProductRequestDTO product, Integer productId) throws ProductNotFoundException {
        log.debug("Enter --> updateProduct with productId: {}", productId);
        return productService.updateProduct(product, productId);
    }

    public boolean deleteProduct(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return productService.deleteProduct(id);
    }

    public Map<String, Object> getTotalRevenueNOrders() {
        return orderService.calculateRevenue();
    }
}
