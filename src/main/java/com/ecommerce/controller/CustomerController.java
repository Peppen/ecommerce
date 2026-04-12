package com.ecommerce.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.dto.CustomerDto;
import com.ecommerce.dto.CustomerResponseDto;
import com.ecommerce.entity.Customer;
import com.ecommerce.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    public final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public CustomerResponseDto findAll(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> pageableCustomers = customerService.findAll(pageable);
        List<CustomerDto> customerDtoList = pageableCustomers.getContent().stream()
                .map(c -> new CustomerDto(c.getUuid(), c.getName(), c.getSurname(), c.getBirthDate(), c.getIdCode(), c.getEmail()))
                .toList();
        return new CustomerResponseDto(customerDtoList);
    }

    @PostMapping
    public CustomerDto create(@RequestBody CustomerDto customer) {
        return customerService.createCustomer(customer.getName(), customer.getSurname(), customer.getBirthDate(), customer.getIdCode(), customer.getEmail());
    }

}
