package com.fresto.service;

import com.fresto.dto.OrderRequestDTO;
import com.fresto.entity.Cart;
import com.fresto.entity.CartItem;
import com.fresto.entity.OrderItem;
import com.fresto.entity.Product;
import com.fresto.exception.ProductUnavailableException;
import com.fresto.repository.CartItemRepository;
import com.fresto.repository.CartRepository;
import com.fresto.repository.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing shopping cart operations.
 */
@Slf4j
@Service
public class CartService {

    private final ProductService productService;
    private final OrderItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;

    public CartService(ProductService productService, OrderItemRepository itemRepository, CartRepository cartRepository, UserService userService, CartItemRepository cartItemRepository) {
        this.productService = productService;
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.cartItemRepository = cartItemRepository;
    }

    public void addProductToCart(OrderRequestDTO orderRequestDTO, CustomUserDetails userDetails) {
        Product product = productService.getProductDetailsById(orderRequestDTO.getProductId());
        if (productService.checkProductAvailability(product, orderRequestDTO.getQuantity())) {
            log.error("product (id: {}) is not available in sufficient quantity {}", product.getProductId(), product.getProductQuantity() - orderRequestDTO.getQuantity());
            throw new ProductUnavailableException("product is not available in sufficient quantity");
        }
        CartItem item = saveCartItem(orderRequestDTO, product, getCart(userDetails));
        saveCart(item, userDetails);
    }

    private CartItem saveCartItem(OrderRequestDTO orderRequestDTO, Product product, Cart cart) {
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(orderRequestDTO.getQuantity());
        item.setPrice(product.getProductPrice());
        item.setCart(cart);
        return cartItemRepository.save(item);
    }

    private void saveCart(CartItem orderItem, CustomUserDetails userDetails) {
        Cart cart = getCart(userDetails);
        List<CartItem> existingItems = cart.getCartItems();
        existingItems.add(orderItem);
        cart.setCartItems(existingItems);
        cart.setTotalAmount(cart.getCartItems().stream().mapToDouble(i -> i.getProduct().getProductPrice() * i.getQuantity()).sum());
        cartRepository.save(cart);
    }

    public Cart getCart(CustomUserDetails userDetails) {
        return cartRepository.findByUserId(userDetails.getId()).orElseGet(()-> {
            Cart newCart = new Cart();
            newCart.setUser(userService.getUserById(userDetails.getId()));
            return cartRepository.save(newCart);
        });
    }

    public void clearCart(CustomUserDetails userDetails) {
        Cart cart = getCart(userDetails);
        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }

    private void removeItemFromCart(Integer orderItemId, CustomUserDetails userDetails) {
        Cart cart = getCart(userDetails);
//
//        List<OrderItem> existingItems = cart.getOrderItems();
//        existingItems.remove(item);
//        cart.setOrderItems(existingItems);
//        cart.setTotalAmount(cart.getOrderItems().stream().mapToDouble(i -> i.getProduct().getProductPrice() * i.getQuantity()).sum());
//        cartRepository.save(cart);
    }
}
