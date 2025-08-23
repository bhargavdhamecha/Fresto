package com.fresto.controller;

import com.fresto.dto.OrderRequestDTO;
import com.fresto.entity.Cart;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.exception.ProductUnavailableException;
import com.fresto.service.CartService;
import com.fresto.service.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.fresto.constant.ApiConstant.CART_PATH;
import static com.fresto.constant.AppConstant.CART_PAGE;

/**
 * Controller class for handling cart-related requests.
 */
@Slf4j
@RequestMapping(CART_PATH)
@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("get-cart")
    public String showUserCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        Cart cart = cartService.getCart(userDetails);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalAmount", cart.getTotalAmount());
        return CART_PAGE;
    }

    @ResponseBody
    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@ModelAttribute OrderRequestDTO orderRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            log.debug("adding to cart: {}", orderRequestDTO);
            cartService.addProductToCart(orderRequestDTO, userDetails);
            log.debug("item added successfully for user: {}", userDetails.getUsername());
            return new ResponseEntity<>("Item added successfully", HttpStatus.OK);
        } catch (ProductNotFoundException | ProductUnavailableException e) {
            log.error("Error adding to cart: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @PostMapping("clear-cart")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            cartService.clearCart(userDetails);
            return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error clearing cart: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
