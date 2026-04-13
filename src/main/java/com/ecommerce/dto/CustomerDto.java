package com.ecommerce.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CustomerDto {

    private UUID id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String idCode;
    private String email;

    public CustomerDto(UUID id, String name, String surname, LocalDate birthDate, String idCode, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.idCode = idCode;
        this.email = email;
    }

    public CustomerDto() {
        // EMPTY CONSTRUCTOR
    }

    public UUID getId() {
        return id;
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

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", idCode='" + idCode + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
