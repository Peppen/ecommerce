package com.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @UuidGenerator
    @Id
    private UUID uuid;
    private String code;
    private String name;
    private int stock;
    @Version // Optimistic Locking for handling concurrency
    private long version;

    public Product(String code, String name, int stock) {
        this.code = code;
        this.name = name;
        this.stock = stock;
    }

    public Product() {
        // EMPTY CONSTRUCTOR
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getVersion() {
        return version;
    }
}
