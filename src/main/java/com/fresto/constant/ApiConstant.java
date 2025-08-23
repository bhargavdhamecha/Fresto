package com.fresto.constant;

/**
 * ApiConstant contains constants for API endpoints and UI paths.
 */
public final class ApiConstant {

    private ApiConstant(){

    }

    // UI page render Calls
    public static final String HOME_PATH = "/home";
    public static final String PRODUCTS_PATH = "/products";
    public static final String LOGIN_PATH = "/login";
    public static final String CART_PATH = "/cart";
    public static final String ABOUT_PATH = "/about";
    public static final String BLOG_PATH = "/blog";
    public static final String CONTACT_PATH = "/contact";
    public static final String SIGN_UP_PATH = "/signup";
    public static final String DASHBOARD_PATH = "/admin-dashboard";
    public static final String LOGIN_PROCESSING_PATH = "/authenticate-user";
    public static final String VIEW_PRODUCT_PATH = "/view-product";
    public static final String DISPLAY_PRODUCTS = "/display-products";
    public static final String PRODUCT_SEARCH = "/search";

    // controller paths
    public static final String ADMIN_CONTROLLER = "/admin";

    // API Endpoints
    public static final String LOGOUT = "/logout";
    public static final String ADD_UPDATE_USER_PATH = "/add-update-user";
    public static final String DELETE_USER = "/delete-user";
    public static final String ADD_UPDATE_PRODUCT_PATH = "/add-update-product";
    public static final String DELETE_PRODUCT = "/delete-product";
    public static final String ADD_TO_CART = "/add-to-cart";
    public static final String REMOVE_FROM_CART = "/remove-from-cart";


}
