package com.ecommerce.dto;

import com.ecommerce.enums.Status;

public class OrderUpdateRequestDto {

    private final Status status;

    public OrderUpdateRequestDto(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

}
