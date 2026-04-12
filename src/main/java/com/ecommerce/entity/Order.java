package com.ecommerce.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import com.ecommerce.enums.Status;

import java.util.UUID;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class Order {

    @UuidGenerator
    @Id
    private UUID uuid;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id_code", referencedColumnName = "id_code")
    private Customer customer;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_code", referencedColumnName = "code")
    private Product product;
    private int stock;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Order(Customer customer, Product product, int stock) {
        this.customer = customer;
        this.product = product;
        this.stock = stock;
        status = Status.INSERTED;
    }

    public Order() {
        // EMPTY CONSTRUCTOR
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getStock() {
        return stock;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
