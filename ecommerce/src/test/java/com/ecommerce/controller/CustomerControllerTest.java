package com.ecommerce.controller;

import com.ecommerce.dto.CustomerDto;
import com.ecommerce.entity.Customer;
import com.ecommerce.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPagedCustomers() throws Exception {
        // GIVEN
        Customer customer = new Customer("Mario", "Rossi", LocalDate.of(1990, 1, 1), "MRARSS90A01H501U", "mario.rossi@example.com");

        Page<Customer> page = new PageImpl<>(List.of(customer));

        Mockito.when(customerService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDtos[0].name").value("Mario"))
                .andExpect(jsonPath("$.customerDtos[0].surname").value("Rossi"))
                .andExpect(jsonPath("$.customerDtos[0].email").value("mario.rossi@example.com"));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        // GIVEN
        CustomerDto inputDto = new CustomerDto(
                UUID.randomUUID(),
                "Luigi",
                "Verdi",
                LocalDate.of(1985, 5, 20),
                "LGIVRD85E20H501X",
                "luigi.verdi@example.com"
        );

        CustomerDto outputDto = new CustomerDto(
                UUID.randomUUID(),
                "Luigi",
                "Verdi",
                LocalDate.of(1985, 5, 20),
                "LGIVRD85E20H501X",
                "luigi.verdi@example.com"
        );

        Mockito.when(customerService.createCustomer(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(LocalDate.class),
                Mockito.anyString(),
                Mockito.anyString()
        )).thenReturn(outputDto);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luigi"))
                .andExpect(jsonPath("$.surname").value("Verdi"))
                .andExpect(jsonPath("$.email").value("luigi.verdi@example.com"));
    }

}
