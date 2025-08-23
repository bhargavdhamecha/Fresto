package com.fresto.utils;


import com.fresto.dto.ProductRequestDTO;
import com.fresto.dto.UserRequestDTO;
import com.fresto.entity.Product;
import com.fresto.entity.User;

public class DtoEntityMapperUtility {

    public static User registerUserDtoToUserEntity(UserRequestDTO registerUserDto) {
        User user = new User();
        user.setUserEmail(registerUserDto.getEmail());
        user.setUserPassword(registerUserDto.getPassword());
        user.setUserName(registerUserDto.getName());
        user.setUserType(registerUserDto.getAdminRights() ? com.fresto.constant.UserType.ADMIN : com.fresto.constant.UserType.USER);
        return user;
    }

    public static Product requestDtoToProductEntity(ProductRequestDTO requestDTO) {
        Product product = new Product();
        product.setProductName(requestDTO.getName());
        product.setProductDesc(requestDTO.getDescription());
        product.setProductPrice(requestDTO.getPrice());
        product.setProductImage(requestDTO.getImageUrl());
        product.setProductCategory(requestDTO.getCategory());
        product.setProductQuantity(requestDTO.getQuantity());
        return product;
    }

    public static UserRequestDTO userEntityToUserRequestDto(User user) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail(user.getUserEmail());
        userRequestDTO.setName(user.getUserName());
        userRequestDTO.setAdminRights(user.getUserType() == com.fresto.constant.UserType.ADMIN);
        return userRequestDTO;
    }

    public static ProductRequestDTO productEntityToProductRequestDto(Product product) {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName(product.getProductName());
        productRequestDTO.setDescription(product.getProductDesc());
        productRequestDTO.setPrice(product.getProductPrice());
        productRequestDTO.setImageUrl(product.getProductImage());
        productRequestDTO.setQuantity(product.getProductQuantity());
        return productRequestDTO;
    }

}
