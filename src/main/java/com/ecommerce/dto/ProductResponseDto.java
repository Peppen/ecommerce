package com.ecommerce.dto;

import java.util.List;

public class ProductResponseDto {

    List<ProductDto> productDtoList;

    public ProductResponseDto(List<ProductDto> productDtoList) {
        this.productDtoList = productDtoList;
    }

    public List<ProductDto> getProductDtoList() {
        return productDtoList;
    }
}
