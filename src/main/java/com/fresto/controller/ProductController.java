package com.fresto.controller;

import com.fresto.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.fresto.constant.ApiConstant.*;
import static com.fresto.constant.AppConstant.*;

/**
 * ProductController handles requests related to product display and details.
 */
@Slf4j
@RequestMapping(PRODUCTS_PATH)
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/o"+DISPLAY_PRODUCTS + "/{category}", "/o"+DISPLAY_PRODUCTS})
    public String displayProducts(Model model, @PathVariable(required = false) String category) {
        log.debug("Enter in display Products with category: {}", category);
        model.addAttribute("page_content", PRODUCTS_PAGE);
        if (category != null) {
            model.addAttribute(PRODUCTS_ATTRIBUTE, productService.getAllProductsByCategory(category));
        } else {
            model.addAttribute(PRODUCTS_ATTRIBUTE, productService.getAllProducts());
        }
        return PRODUCTS_PAGE;
    }

    @GetMapping(VIEW_PRODUCT_PATH + "/{productId}")
    public String viewSingleProduct(Model model, HttpServletRequest request, @PathVariable Integer productId) {
        model.addAttribute(PRODUCT, productService.getProductDetailsById(productId));
        return VIEW_PRODUCT_PAGE;
    }

    @GetMapping({"/o" + PRODUCT_SEARCH})
    public String searchProducts(Model model, @RequestParam String query){
        log.debug("Enter in searchProducts with query: {}", query);
        model.addAttribute("page_content", PRODUCTS_PAGE);
        model.addAttribute(PRODUCTS_ATTRIBUTE, productService.searchProductsByNameOrDescription(query));
        return PRODUCTS_PAGE;
    }
}
