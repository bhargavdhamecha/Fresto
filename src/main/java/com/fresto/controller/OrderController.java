package com.fresto.controller;

import com.fresto.dto.OrderRequestDTO;
import com.fresto.entity.Orders;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.exception.ProductUnavailableException;
import com.fresto.service.CustomUserDetails;
import com.fresto.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.fresto.constant.ApiConstant.*;
import static com.fresto.constant.AppConstant.CART_PAGE;
import static com.fresto.constant.AppConstant.CHECKOUT_PAGE;

/**
 * Controller class for handling order-related requests.
 */
@Slf4j
@Controller
@RequestMapping(ORDER_CONTROLLER)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(PLACE_ORDER)
    public ResponseEntity<String> addOrder(@ModelAttribute OrderRequestDTO orderRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            log.debug("placing order with orderValue: {}", orderRequestDTO);
            orderService.placeOrder(orderRequestDTO, userDetails);
            log.debug("order placed successfully for user: {}", userDetails.getUsername());
            return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
        } catch (ProductNotFoundException | ProductUnavailableException e) {
            log.error("Error placing order: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(CHECKOUT)
    public String checkout(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        try {
            Orders order = orderService.checkout(userDetails);
            model.addAttribute("order", order);
            return CHECKOUT_PAGE; // Thymeleaf template
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return CART_PAGE; // go back to cart if empty
        }
    }
}
