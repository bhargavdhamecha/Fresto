package com.fresto.dto;

import com.fresto.constant.ProductCategory;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductRequestDTO {

    @NotNull
    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private double price;
    private int quantity;
    private String imageUrl;
    private ProductCategory category;
}
