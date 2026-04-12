package com.ecommerce.services;

import com.ecommerce.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.ecommerce.dto.CustomerDto;
import com.ecommerce.entity.Customer;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.repository.CustomerRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService sut;

    @Test
    void findAllShouldReturnPagedCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(
                new Customer("Mario", "Rossi", LocalDate.of(1990, 1, 1), "ABC123", "mario@mail.com")
        );
        Page<Customer> page = new PageImpl<>(customers, pageable, customers.size());

        Mockito.when(customerRepository.findAll(pageable)).thenReturn(page);

        Page<Customer> result = sut.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Mario", result.getContent().getFirst().getName());
        Mockito.verify(customerRepository).findAll(pageable);
    }

    @Test
    void createCustomerWhenIdCodeNotExists() {
        String idCode = "XYZ789";
        Mockito.when(customerRepository.findByIdCode(idCode)).thenReturn(Optional.empty());

        Customer saved = new Customer("Luca", "Bianchi",
                LocalDate.of(1985, 5, 20), idCode, "luca@mail.com");

        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(saved);

        CustomerDto result = sut.createCustomer(
                "Luca", "Bianchi", LocalDate.of(1985, 5, 20), idCode, "luca@mail.com"
        );

        assertEquals("Luca", result.getName());
        assertEquals("Bianchi", result.getSurname());
        assertEquals(idCode, result.getIdCode());
        Mockito.verify(customerRepository).findByIdCode(idCode);
        Mockito.verify(customerRepository).save(Mockito.any(Customer.class));
    }

    @Test
    void createCustomerWhenCustomerAlreadyExists() {
        String idCode = "DUPLICATE123";
        Customer existing = new Customer("Marco", "Verdi",
                LocalDate.of(1992, 3, 10), idCode, "marco@mail.com");

        Mockito.when(customerRepository.findByIdCode(idCode))
                .thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () ->
                sut.createCustomer(
                        "Marco", "Verdi", LocalDate.of(1992, 3, 10), idCode, "marco@mail.com"
                )
        );

        Mockito.verify(customerRepository).findByIdCode(idCode);
        Mockito.verify(customerRepository, Mockito.never()).save(Mockito.any());
    }
}
