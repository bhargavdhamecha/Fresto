package com.fresto.service;

import com.fresto.dto.OrderRequestDTO;
import com.fresto.entity.*;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.exception.ProductUnavailableException;
import com.fresto.repository.OrderItemRepository;
import com.fresto.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository repository, OrderItemRepository itemRepository,
                        ProductService productService, UserService userService, CartService cartService) {
        this.orderRepository = repository;
        this.itemRepository = itemRepository;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }


    @Transactional
    public void placeOrder(OrderRequestDTO orderRequestDTO, CustomUserDetails userDetails) throws ProductNotFoundException, ProductUnavailableException {
        Product product = productService.getProductDetailsById(orderRequestDTO.getProductId());
        if (productService.checkProductAvailability(product, orderRequestDTO.getQuantity())) {
            log.error("product (id: {}) is not available in sufficient quantity {}", product.getProductId(), product.getProductQuantity() - orderRequestDTO.getQuantity());
            throw new ProductUnavailableException("product is not available in sufficient quantity");
        }
        OrderItem item = saveOrderItem(orderRequestDTO, product);
        saveOrder(item, userDetails);
        updateProductQuantityOnOrder(orderRequestDTO, product);
    }

    private OrderItem saveOrderItem(OrderRequestDTO orderRequestDTO, Product product) {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(orderRequestDTO.getQuantity());
        item.setPrice(product.getProductPrice());
        return itemRepository.save(item);
    }

    private void updateProductQuantityOnOrder(OrderRequestDTO orderRequestDTO, Product product) {
        if (orderRequestDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Insufficient product quantity for order.");
        }
        product.setProductQuantity(product.getProductQuantity() - orderRequestDTO.getQuantity());
        productService.updateProduct(product, product.getProductId());
    }

    private void saveOrder(OrderItem orderItem, CustomUserDetails userDetails) {
        Orders order = new Orders();
        order.setOrderDate(LocalDateTime.now());
        order.setItems(List.of(orderItem));
        order.setUser(userService.getUserById(userDetails.getId()));
        order.setTotalAmount(calculateTotalAmount(order.getItems()));
        orderRepository.save(order);
    }

    public Double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    @Secured("ROLE_ADMIN")
    public Map<String, Object> calculateRevenue() {
        List<Orders> orders = orderRepository.findAll();
        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("totalOrders", orders.size());
        revenueData.put("totalRevenue",
                orders.stream()
                        .mapToDouble(Orders::getTotalAmount)
                        .sum());
        return revenueData;
    }


    public Orders checkout(CustomUserDetails userDetails) {
        Cart cart = cartService.getCart(userDetails);

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty, cannot checkout!");
        }

        Orders order = new Orders();
        order.setUser(userService.getUserById(userDetails.getId()));
        order.setOrderDate(LocalDateTime.now());

        double total = 0.0;

        for (CartItem ci : cart.getCartItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getProductPrice()); // snapshot
            order.getItems().add(oi);

            total += ci.getQuantity() * ci.getProduct().getProductPrice();
        }

        order.setTotalAmount(total);

        // Save order
        Orders savedOrder = orderRepository.save(order);

        // Clear cart
        cartService.clearCart(userDetails);

        return savedOrder;
    }
}
