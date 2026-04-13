package com.ecommerce.dto;

import java.util.UUID;

public class ProductDto {

    private UUID id;
    private String code;
    private String name;
    private int stock;
    private long version;

    public ProductDto(UUID id, String code, String name, int stock, long version) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stock = stock;
        this.version = version;
    }

    public ProductDto() {
        // EMPTY CONSTRUCTOR
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", version=" + version +
                '}';
    }
}
