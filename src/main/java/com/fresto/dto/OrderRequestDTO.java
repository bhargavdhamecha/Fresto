package com.fresto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * OrderRequestDTO is a Data Transfer Object (DTO) for handling order requests.
 */
@Getter
@Setter
@AllArgsConstructor
public class OrderRequestDTO {
    private int productId;
    private int quantity;
}
