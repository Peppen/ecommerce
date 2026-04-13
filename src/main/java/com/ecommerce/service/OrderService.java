package com.ecommerce.service;

import com.ecommerce.dto.CustomerDto;
import com.ecommerce.dto.ProductDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.Customer;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.exception.NotFoundException;
import com.ecommerce.exception.InternalServerException;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.dto.OrderUpdateRequestDto;
import com.ecommerce.enums.Status;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public OrderDto createOrder(String customerIdCode, String productCode, int stock) {
        Optional<Customer> customer = customerRepository.findByIdCode(customerIdCode);
        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Optional<Product> product = productRepository.findByCode(productCode);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        Customer realCustomer = customer.get();
        Product realProduct = product.get();

        if (realProduct.getStock() < stock) {
            throw new InternalServerException("Requested stock not available");
        }
        Order order = new Order(realCustomer, realProduct, stock);
        order.setStatus(Status.INSERTED);
        realProduct.setStock(realProduct.getStock() - stock);
        productRepository.save(realProduct);
        Order saved = orderRepository.save(order);
        Customer c = saved.getCustomer();
        CustomerDto customerDto = new CustomerDto(c.getUuid(), c.getName(), c.getSurname(), c.getBirthDate(), c.getIdCode(), c.getEmail());
        ProductDto productDto = new ProductDto(saved.getProduct().getUuid(), saved.getProduct().getCode(), saved.getProduct().getName(), saved.getProduct().getStock(), saved.getProduct().getVersion());
        return new OrderDto(saved.getUuid(), customerDto, productDto, saved.getStock(), saved.getStatus());
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public boolean deleteOrder(UUID orderId) {
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if (orderToDelete.isEmpty()) {
            throw new NotFoundException("Order not found");
        } else if (orderToDelete.get().getStatus().equals(Status.DELIVERED)) {
            throw new InternalServerException("Order already delivered");
        }
        orderRepository.deleteById(orderId);
        return true;
    }

    @Transactional
    public OrderDto updateOrder(UUID uuid, OrderUpdateRequestDto orderUpdateRequest) {
        Optional<Order> orderToUpdate = orderRepository.findById(uuid);
        if (orderToUpdate.isEmpty()) {
            throw new NotFoundException("Order not found");
        } else {
            Order realOrder = orderToUpdate.get();
            if(realOrder.getStatus().equals(Status.DELIVERED)) {
                throw new ConflictException("Order already delivered");
            } else if(realOrder.getStatus().equals(Status.INSERTED) && orderUpdateRequest.getStatus().equals(Status.INSERTED)) {
                throw new ConflictException("Order already inserted!");
            }
            realOrder.setStatus(orderUpdateRequest.getStatus());
            Customer customer_1 = realOrder.getCustomer();
            CustomerDto customerDto = new CustomerDto(customer_1.getUuid(), customer_1.getName(), customer_1.getSurname(), customer_1.getBirthDate(), customer_1.getIdCode(), customer_1.getEmail());
            ProductDto productDto = new ProductDto(realOrder.getProduct().getUuid(), realOrder.getProduct().getCode(), realOrder.getProduct().getName(), realOrder.getProduct().getStock(), realOrder.getProduct().getVersion());
            return new OrderDto(realOrder.getUuid(), customerDto, productDto, realOrder.getStock(), realOrder.getStatus());
        }
    }

    public Optional<Order> findById(UUID uuid) {
        return orderRepository.findById(uuid);
    }
}
