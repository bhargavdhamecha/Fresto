package com.fresto.utils;

/**
 * StringUtils provides utility methods for string manipulation and formatting.
 * It includes methods for formatting prices to a standard two-decimal format.
 */
public class StringUtils {

    private StringUtils() {
        // Prevent instantiation
    }

    public static String priceFormatter(Double price) {
        if (price == null) {
            return "0.00";
        }
        return String.format("%.2f", price);
    }

}
