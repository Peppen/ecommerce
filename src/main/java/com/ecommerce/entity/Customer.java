package com.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @UuidGenerator
    @Id
    private UUID uuid;
    private String name;
    private String surname;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "id_code")
    private String idCode;
    private String email;

    public Customer(String name, String surname, LocalDate birthDate, String idCode, String email) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.idCode = idCode;
        this.email = email;
    }

    public Customer() {
        // EMPTY CONSTRUCTOR
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getIdCode() {
        return idCode;
    }

    public String getEmail() {
        return email;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


}
