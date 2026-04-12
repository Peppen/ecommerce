package com.ecommerce.consumer;

import com.ecommerce.configuration.RabbitConfig;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.Order;
import com.ecommerce.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MessageConsumer {

    private final OrderService orderService;

    public MessageConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receiveMessage(UUID orderId) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isPresent()) {
            Order finalOrder = order.get();
            OrderDto orderDto = new OrderDto(finalOrder.getUuid(), finalOrder.getCustomer(), finalOrder.getProduct(), finalOrder.getStock(), finalOrder.getStatus());
            System.out.println("Ricevuto: " + orderDto);
        }
    }
}
