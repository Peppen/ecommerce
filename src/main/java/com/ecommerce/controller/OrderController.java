package com.ecommerce.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.OrderResponseDto;
import com.ecommerce.entity.Order;
import com.ecommerce.dto.CustomerOrderRequestDto;
import com.ecommerce.dto.OrderUpdateRequestDto;
import com.ecommerce.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public OrderResponseDto findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.findAll(pageable);
        List<OrderDto> orderDtoList = orders.stream().map(o -> new OrderDto(o.getUuid(), o.getCustomer(), o.getProduct(), o.getStock(), o.getStatus())).toList();
        return new OrderResponseDto(orderDtoList);
    }

    @PostMapping
    public OrderDto create(@RequestBody CustomerOrderRequestDto request) {
        return orderService.createOrder(request.getCustomer(), request.getProduct(), request.getStock());
    }

    @DeleteMapping("/{orderId}")
    public boolean delete(@PathVariable UUID orderId) {
        return orderService.deleteOrder(orderId);
    }

    @PatchMapping("/{orderId}")
    public OrderDto update(@PathVariable UUID orderId, @RequestBody OrderUpdateRequestDto request) {
        return orderService.updateOrder(orderId, request);
    }
}
