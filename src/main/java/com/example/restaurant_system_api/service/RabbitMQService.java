package com.example.restaurant_system_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQService {

    private String queueName;
    private static final String HOST = "localhost";
    private final ConnectionFactory factory;
    private final ObjectMapper objectMapper;
    private Connection connection;
    private Channel channel;

    public RabbitMQService(String queueName) {
        this.queueName = queueName;
        this.factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            this.connection = factory.newConnection();
            this.channel = connection.createChannel();
            this.channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
        }

    }

    public void sendObjectToQueue(Object obj) throws Exception {
        String jsonMessage = objectMapper.writeValueAsString(obj);
        channel.basicPublish("", queueName, null, jsonMessage.getBytes());
    }


    // clode channel and connection
    public void close() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
