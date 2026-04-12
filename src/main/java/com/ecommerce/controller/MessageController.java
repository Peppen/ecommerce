package com.ecommerce.controller;

import com.ecommerce.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    @Autowired
    private MessageProducer producer;

    @GetMapping("/send")
    public String send(@RequestParam UUID orderId) {
        producer.sendMessage(orderId);
        return "Messaggio inviato!";
    }
}
