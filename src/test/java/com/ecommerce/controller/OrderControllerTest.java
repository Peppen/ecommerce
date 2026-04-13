package com.ecommerce.controller;

import com.ecommerce.dto.*;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.Status;
import com.ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private Customer customer;
    private Product product;

    private CustomerDto customerDto;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
        customer = new Customer("Mario", "Rossi", LocalDate.of(1990, 1, 1), "MRARSS90A01H501U", "mario.rossi@example.com");
        product = new Product("PRD001", "Laptop", 12);
        productDto = new ProductDto(UUID.randomUUID(), product.getCode(), product.getName(), product.getStock(), product.getVersion());
        customerDto = new CustomerDto(UUID.randomUUID(), customer.getName(), customer.getSurname(), customer.getBirthDate(), customer.getIdCode(), customer.getEmail());
    }

    @Test
    void shouldReturnPagedOrders() throws Exception {
        // GIVEN
        Order order = new Order(customer, product, 8);

        Page<Order> page = new PageImpl<>(List.of(order));

        Mockito.when(orderService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderDtos[0].customer.name").value(customer.getName()))
                .andExpect(jsonPath("$.orderDtos[0].product.name").value(product.getName()))
                .andExpect(jsonPath("$.orderDtos[0].stock").value(8))
                .andExpect(jsonPath("$.orderDtos[0].status").value("INSERTED"));
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // GIVEN
        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto("MRARSS90A01H501U", "PRD001", 8);

        OrderDto responseDto = new OrderDto(UUID.randomUUID(), customerDto, productDto, 5, Status.INSERTED);

        Mockito.when(orderService.createOrder(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        )).thenReturn(responseDto);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.name").value(customer.getName()))
                .andExpect(jsonPath("$.product.name").value(product.getName()))
                .andExpect(jsonPath("$.stock").value(5))
                .andExpect(jsonPath("$.status").value("INSERTED"));
    }

    @Test
    void shouldDeleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderDto responseDto = new OrderDto(orderId, customerDto, productDto, 5, Status.INSERTED);

        Mockito.when(orderService.createOrder(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        )).thenReturn(responseDto);

        Mockito.when(orderService.deleteOrder(orderId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldNotUpdateOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderDto updatedOrder = new OrderDto(orderId, customerDto, productDto, 5, Status.INSERTED);
        Mockito.when(orderService.findById(orderId)).thenReturn(null);

        OrderUpdateRequestDto updateRequest = new OrderUpdateRequestDto(Status.DELIVERED);

        Mockito.when(orderService.updateOrder(
                        Mockito.eq(orderId),
                        Mockito.any(OrderUpdateRequestDto.class)))
                .thenReturn(updatedOrder);

        mockMvc.perform(patch("/orders/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().is4xxClientError());
    }

}
