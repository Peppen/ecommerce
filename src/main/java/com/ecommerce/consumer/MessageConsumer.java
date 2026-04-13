package com.ecommerce.consumer;

import com.ecommerce.configuration.RabbitConfig;
import com.ecommerce.dto.CustomerDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.ProductDto;
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
            CustomerDto customerDto = new CustomerDto(finalOrder.getCustomer().getUuid(), finalOrder.getCustomer().getName(),
                    finalOrder.getCustomer().getSurname(), finalOrder.getCustomer().getBirthDate(), finalOrder.getCustomer().getIdCode(),
                    finalOrder.getCustomer().getEmail());
            ProductDto productDto = new ProductDto(finalOrder.getProduct().getUuid(), finalOrder.getProduct().getCode(), finalOrder.getProduct().getName(),
                    finalOrder.getProduct().getStock(), finalOrder.getProduct().getVersion());
            OrderDto orderDto = new OrderDto(finalOrder.getUuid(), customerDto, productDto, finalOrder.getStock(), finalOrder.getStatus());
            System.out.println("Ricevuto: " + orderDto);
        }
    }
}
