package com.ecommerce.dto;

import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.Status;

import java.util.UUID;

public class OrderDto {

    private UUID id;
    private CustomerDto customer;
    private ProductDto product;
    private int stock;
    private Status status;

    public OrderDto(UUID id, CustomerDto customer, ProductDto product, int stock, Status status) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.stock = stock;
        this.status = status;
    }

    public OrderDto() {
        // EMPTY CONSTRUCTOR
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public int getStock() {
        return stock;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", customer=" + customer +
                ", product=" + product +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}
