package com.ecommerce.dto;

import java.util.List;

public class OrderResponseDto {

    private List<OrderDto> orderDtos;

    public OrderResponseDto(List<OrderDto> orderDtos) {
        this.orderDtos = orderDtos;
    }

    public List<OrderDto> getOrderDtos() {
        return orderDtos;
    }
}
