package com.fresto.controller;

import com.fresto.dto.ProductRequestDTO;
import com.fresto.dto.UserRequestDTO;
import com.fresto.entity.Product;
import com.fresto.exception.ProductNotFoundException;
import com.fresto.exception.UserNotFoundException;
import com.fresto.service.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.fresto.constant.ApiConstant.*;
import static com.fresto.constant.AppConstant.*;

/**
 * AdminController is responsible for handling requests related to admin functionalities.
 */
@Slf4j
@RequestMapping(ADMIN_CONTROLLER)// Handles exceptions globally for this controller
@Secured("ROLE_ADMIN") // Ensures that only users with the ADMIN role can access
@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping(DASHBOARD_PATH)
    public String routeToAdminDashboard(Model model) {
        log.info("Accessing admin dashboard");
        Map<String, Object> totalRevenue = adminService.getTotalRevenueNOrders();
        List<Product> productList = adminService.getAllProducts();
        int totalProducts = productList.size();
        // need to pass all admin user, current users, and all products to the view
        model.addAttribute("totalOrders", totalRevenue.get("totalOrders"));
        model.addAttribute("totalCustomers", adminService.getAllCustomerUserCount());
        model.addAttribute("productsInStock", totalProducts);
        model.addAttribute("totalRevenue", totalRevenue.get("totalRevenue"));
        model.addAttribute("orders", adminService.getAllOrders());
        model.addAttribute("lowStockProducts", adminService.getLowStockProducts());
        model.addAttribute("products", productList);

//        model.addAttribute("adminUsers", adminService.getAllAdminUsers());
//        model.addAttribute("customerUsers", adminService.getAllCustomerUsers());
        return ADMIN_DASHBOARD_PAGE; // Returns the view name for the admin dashboard
    }

    @GetMapping({ADD_UPDATE_USER_PATH, ADD_UPDATE_USER_PATH + "/{id}"})
    public String routeToAddUser(Model model, @PathVariable(required = false) Integer id) {
        try {
            if (id != null) {
                log.info("Editing user with ID: {}", id);
                Map<String, Object> map = adminService.getUserDetailsById(id);
                model.addAttribute(USER, map.get(USER));
                model.addAttribute(REQUEST_DTO, map.get(REQUEST_DTO));
            } else {
                model.addAttribute(USER, null); // Initialize a new UserRequestDTO for adding a user
                model.addAttribute(REQUEST_DTO, new UserRequestDTO()); // Add a DTO for user request
            }
            return ADD_UPDATE_USER_PAGE;
        } catch (UserNotFoundException | IllegalArgumentException e) {
            log.error("Error retrieving user details: {}", e.getMessage());
            model.addAttribute("error", "User not found or invalid ID");
            return REDIRECT_TO + EXCEPTION_PAGE; // Redirect to an error page
        }
    }

    @GetMapping({ADD_UPDATE_PRODUCT_PATH, ADD_UPDATE_PRODUCT_PATH + "/{id}"})
    public String routeToAddProduct(Model model, @PathVariable(required = false) Integer id) {
        try {
            if (id != null) {
                log.info("Editing user with ID: {}", id);
                Map<String, Object> map = adminService.getProductDetailsById(id);
                model.addAttribute(PRODUCT, map.get(PRODUCT));
                model.addAttribute(REQUEST_DTO, map.get(REQUEST_DTO));
            } else {
                model.addAttribute(PRODUCT, null); // Initialize a new UserRequestDTO for adding a user
                model.addAttribute(REQUEST_DTO, new ProductRequestDTO()); // Add a DTO for user request
            }
            return ADD_UPDATE_PRODUCT_PAGE;
        } catch (ProductNotFoundException | IllegalArgumentException e) {
            log.error("Error retrieving product details: {}", e.getMessage());
            model.addAttribute("error", "Product not found or invalid ID");
            return REDIRECT_TO + EXCEPTION_PAGE; // Redirect to an error page
        }
    }


    @PostMapping({ADD_UPDATE_USER_PATH, ADD_UPDATE_USER_PATH + "/{id}"})
    public String addUpdateUser(@Valid @ModelAttribute UserRequestDTO requestDTO, @PathVariable(required = false) Integer id) {
        log.debug("entered -> addUpdateUser() with id: {}, email: {}", id, requestDTO.getEmail());
        boolean isUserAdded = false;
        try {
            if (id != null) {
                isUserAdded = adminService.updateUser(requestDTO, id);
            } else {
                log.info("Adding new user: {}", requestDTO.getEmail());
                isUserAdded = adminService.addUser(requestDTO);
            }
        } catch (Exception e) {
            log.error("Error adding user: {}", e.getMessage());
        }
        log.debug("User details are changed: {}", isUserAdded);
        if (isUserAdded) {
            return REDIRECT_TO + ADMIN + DASHBOARD_PATH;
        }
        return EXCEPTION_PAGE;
    }


    @PostMapping(DELETE_USER + "/{id}")
    public String deleteUser(@PathVariable Integer id) {
        if (adminService.deleteUser(id)) {
            return REDIRECT_TO + ADMIN + DASHBOARD_PATH;
        }
        return EXCEPTION_PAGE;
    }


    @PostMapping({ADD_UPDATE_PRODUCT_PATH, ADD_UPDATE_PRODUCT_PATH + "/{id}"})
    public String addUpdateProduct(@Valid @ModelAttribute ProductRequestDTO requestDTO, @PathVariable(required = false) Integer id) {
        log.debug("entered -> addUpdateProduct() with id: {}, name: {}", id, requestDTO.getName());
        boolean isProductAdded = false;
        try {
            if (id != null) {
                isProductAdded = adminService.updateProduct(requestDTO, id);
            } else {
                log.info("Adding new product: {}", requestDTO.getName());
                isProductAdded = adminService.addProduct(requestDTO);
            }
        } catch (Exception e) {
            log.error("Error adding product: {}", e.getMessage());
        }
        if (isProductAdded) {
            return REDIRECT_TO + ADMIN + DASHBOARD_PATH;
        }
        return REDIRECT_TO + ADMIN + DASHBOARD_PATH + "?error=Failed to add product";
    }


    @PostMapping(DELETE_PRODUCT + "/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        if (adminService.deleteProduct(id)) {
            return REDIRECT_TO + ADMIN + DASHBOARD_PATH;
        }
        return EXCEPTION_PAGE;
    }

}

