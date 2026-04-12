package com.ecommerce.dto;

import java.util.List;

public class CustomerResponseDto {

    private List<CustomerDto> customerDtos;

    public CustomerResponseDto(List<CustomerDto> customerDtos) {
        this.customerDtos = customerDtos;
    }

    public List<CustomerDto> getCustomerDtos() {
        return customerDtos;
    }
}
