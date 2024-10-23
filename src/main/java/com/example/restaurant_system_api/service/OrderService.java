package com.example.restaurant_system_api.service;

import com.example.restaurant_system_api.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class OrderService {

    RabbitMQService rabbitMQService = new RabbitMQService();
    public OrderService() {
    }

    @CrossOrigin
    @PostMapping("/restaurant/order/add")
    public ResponseEntity<String> addOrder (@RequestBody Order order) throws Exception {
        order.setStatus("placed");
        order.setOrderTime(LocalDateTime.now());
        rabbitMQService.sendObjectToQueue(order);
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body("success");
    }

}
