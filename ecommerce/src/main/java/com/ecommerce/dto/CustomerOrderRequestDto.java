package com.ecommerce.dto;

public class CustomerOrderRequestDto {
    
    private String customerIdCode;
    private String productCode;
    private int stock;

    public CustomerOrderRequestDto(String customerIdCode, String productCode, int stock) {
        this.customerIdCode = customerIdCode;
        this.productCode = productCode;
        this.stock = stock;
    }

    public String getCustomer() {
        return customerIdCode;
    }

    public void setCustomer(String customerIdCode) {
        this.customerIdCode = customerIdCode;
    }

    public String getProduct() {
        return productCode;
    }

    public void setProduct(String productCode) {
        this.productCode = productCode;
    }

    public int getStock() {
        return stock;
    }
}
