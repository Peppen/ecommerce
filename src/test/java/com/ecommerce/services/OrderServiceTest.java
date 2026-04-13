package com.ecommerce.services;

import com.ecommerce.service.OrderService;
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
import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.exception.InternalServerException;
import com.ecommerce.exception.NotFoundException;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.dto.OrderUpdateRequestDto;
import com.ecommerce.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService sut;

    @Test
    void createOrderWhenCustomerNotExistsShouldThrowNotFound() {
        Mockito.when(customerRepository.findByIdCode("CUST1"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sut.createOrder("CUST1", "PROD1", 5)
        );

        Mockito.verify(customerRepository).findByIdCode("CUST1");
        Mockito.verify(productRepository, Mockito.never()).findByCode(Mockito.any());
    }

    @Test
    void createOrderWhenProductNotExistsShouldThrowNotFound() {
        Customer customer = new Customer("Mario", "Rossi", LocalDate.now(), "CUST1", "mail@mail.com");

        Mockito.when(customerRepository.findByIdCode("CUST1"))
                .thenReturn(Optional.of(customer));
        Mockito.when(productRepository.findByCode("PROD1"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sut.createOrder("CUST1", "PROD1", 5)
        );

        Mockito.verify(productRepository).findByCode("PROD1");
    }

    @Test
    void createOrderWhenStockNotAvailableShouldThrowInternalServer() {
        Customer customer = new Customer("Mario", "Rossi", LocalDate.now(), "CUST1", "mail@mail.com");
        Product product = new Product("PROD1", "Prodotto", 3);

        Mockito.when(customerRepository.findByIdCode("CUST1"))
                .thenReturn(Optional.of(customer));
        Mockito.when(productRepository.findByCode("PROD1"))
                .thenReturn(Optional.of(product));

        assertThrows(InternalServerException.class, () ->
                sut.createOrder("CUST1", "PROD1", 10)
        );
    }

    @Test
    void createOrderShouldCreateOrderSuccessfully() {
        Customer customer = new Customer("Mario", "Rossi", LocalDate.now(), "CUST1", "mail@mail.com");
        Product product = new Product("PROD1", "Prodotto", 10);

        Order savedOrder = new Order(customer, product, 5);
        savedOrder.setStatus(Status.INSERTED);

        Mockito.when(customerRepository.findByIdCode("CUST1"))
                .thenReturn(Optional.of(customer));
        Mockito.when(productRepository.findByCode("PROD1"))
                .thenReturn(Optional.of(product));
        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenReturn(savedOrder);

        OrderDto result = sut.createOrder("CUST1", "PROD1", 5);

        assertEquals(Status.INSERTED, result.getStatus());
        assertEquals(5, result.getStock());
        assertEquals("PROD1", result.getProduct().getCode());
        assertEquals(5, product.getStock());

        Mockito.verify(orderRepository).save(Mockito.any(Order.class));
    }

    @Test
    void findAllShouldReturnPagedOrders() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Order> orders = List.of(Mockito.mock(Order.class));
        Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

        Mockito.when(orderRepository.findAll(pageable)).thenReturn(page);

        Page<Order> result = sut.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        Mockito.verify(orderRepository).findAll(pageable);
    }

    @Test
    void deleteOrderWhenOrderNotExistsShouldThrowConflict() {
        UUID id = UUID.randomUUID();

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sut.deleteOrder(id)
        );
    }

    @Test
    void deleteOrderWhenOrderDeliveredShouldThrowInternalServer() {
        UUID id = UUID.randomUUID();
        Order order = Mockito.mock(Order.class);

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(order.getStatus()).thenReturn(Status.DELIVERED);

        assertThrows(InternalServerException.class, () ->
                sut.deleteOrder(id)
        );
    }

    @Test
    void deleteOrderShouldDeleteSuccessfully() {
        UUID id = UUID.randomUUID();
        Order order = Mockito.mock(Order.class);

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(order.getStatus()).thenReturn(Status.INSERTED);

        boolean result = sut.deleteOrder(id);

        assertTrue(result);
        Mockito.verify(orderRepository).deleteById(id);
    }

    @Test
    void updateOrderWhenOrderNotFoundShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sut.updateOrder(id, new OrderUpdateRequestDto(Status.DELIVERED))
        );
    }

    @Test
    void updateOrderWhenOrderDeliveredShouldThrowConflictException() {
        UUID id = UUID.randomUUID();
        Order order = Mockito.mock(Order.class);

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(order.getStatus()).thenReturn(Status.DELIVERED);

        assertThrows(ConflictException.class, () ->
                sut.updateOrder(id, new OrderUpdateRequestDto(Status.INSERTED))
        );
    }

    @Test
    void updateOrderShouldUpdateSuccessfully() {
        UUID id = UUID.randomUUID();
        Customer customer = Mockito.mock(Customer.class);
        Product product = Mockito.mock(Product.class);

        Order order = new Order(customer, product, 5);
        order.setStatus(Status.INSERTED);

        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        OrderUpdateRequestDto req = new OrderUpdateRequestDto(Status.DELIVERED);

        OrderDto result = sut.updateOrder(id, req);

        assertEquals(Status.DELIVERED, result.getStatus());
    }
}
