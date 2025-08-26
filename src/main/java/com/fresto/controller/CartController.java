package com.fresto.controller;

import com.fresto.dto.OrderRequestDTO;
import com.fresto.entity.Cart;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.exception.ProductUnavailableException;
import com.fresto.service.CartService;
import com.fresto.service.CustomUserDetails;
import com.fresto.service.ProductService;
import com.fresto.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.fresto.constant.ApiConstant.*;
import static com.fresto.constant.AppConstant.*;

/**
 * Controller class for handling cart-related requests.
 */
@Slf4j
@RequestMapping(CART_CONTROLLER)
@Controller
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping(GET_CART)
    public String showUserCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        Cart cart = cartService.getCart(userDetails);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalAmount", StringUtils.priceFormatter(cart.getTotalAmount()));
        return CART_PAGE;
    }

    @PostMapping(ADD_TO_CART)
    public String addToCart(@ModelAttribute OrderRequestDTO orderRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            log.debug("adding to cart: {}", orderRequestDTO);
            cartService.addProductToCart(orderRequestDTO, userDetails);
            log.debug("item added successfully for user: {}", userDetails.getUsername());
            model.addAttribute(SUCCESS_MESSAGE, "Item added to cart successfully!");
            model.addAttribute(PRODUCTS_ATTRIBUTE, productService.getAllProducts());
            return PRODUCTS_PAGE;
        } catch (ProductNotFoundException | ProductUnavailableException e) {
            log.error("Error adding to cart: {}", e.getMessage());
            return EXCEPTION_PAGE;
        }
    }

    @PostMapping(CLEAR_CART)
    public String clearCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            cartService.clearCart(userDetails);
            model.addAttribute(SUCCESS_MESSAGE, "Cart cleared successfully!");
            return CART_PAGE;
        } catch (Exception e) {
            log.error("Error clearing cart: {}", e.getMessage());
            return EXCEPTION_PAGE;
        }
    }

    @PostMapping(REMOVE_FROM_CART)
    public String removeFromCart(@RequestParam Long cartItemId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            Cart cart = cartService.removeProductFromCart(cartItemId, userDetails);
            model.addAttribute(SUCCESS_MESSAGE, "Item removed from cart successfully!");
            model.addAttribute("cartItems", cart.getCartItems());
            model.addAttribute("totalAmount", cart.getTotalAmount());
            return CART_PAGE;
        } catch (ProductNotFoundException e) {
            log.error("Error removing from cart: {}", e.getMessage());
            return EXCEPTION_PAGE;
        }
    }



}
