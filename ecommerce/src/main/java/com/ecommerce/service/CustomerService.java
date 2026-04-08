package com.ecommerce.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.dto.CustomerDto;
import com.ecommerce.entity.Customer;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.repository.CustomerRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public CustomerDto createCustomer(String name, String surname, LocalDate birthDate, String idCode, String email) {
        Optional<Customer> existentCustomer = customerRepository.findByIdCode(idCode);
        if (existentCustomer.isPresent()) {
            throw new ConflictException("Customer already registered");
        }
        Customer customer = new Customer(name, surname, birthDate, idCode, email);
        Customer saved = customerRepository.save(customer);
        return new CustomerDto(saved.getUuid(), saved.getName(), saved.getSurname(), saved.getBirthDate(), saved.getIdCode(), saved.getEmail());
    }

}
