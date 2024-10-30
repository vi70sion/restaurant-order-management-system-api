package com.example.restaurant_system_api.service;

import com.example.restaurant_system_api.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class OrderService {

    RabbitMQService rabbitMQServicePlaceOrder = new RabbitMQService("orders_queue");
    RabbitMQService rabbitMQServicePlacePayment = new RabbitMQService("payment_queue");
    public OrderService() {
    }

    @CrossOrigin
    @PostMapping("/restaurant/order/add")
    public ResponseEntity<String> addOrder (@RequestBody Order order) throws Exception {
        order.setStatus("placed");
        order.setOrderTime(LocalDateTime.now());
        rabbitMQServicePlaceOrder.sendObjectToQueue(order);
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body("success");
    }

    @CrossOrigin
    @PostMapping("/restaurant/order/{orderId}/payment")
    public ResponseEntity<String> addPayment (@PathVariable String orderId, @RequestBody Order order) throws Exception {
        order.setStatus("paid");
        rabbitMQServicePlacePayment.sendObjectToQueue(order);
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body("success");
    }

}
