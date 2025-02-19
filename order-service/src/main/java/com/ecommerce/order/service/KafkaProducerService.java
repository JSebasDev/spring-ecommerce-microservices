package com.ecommerce.order.service;

import com.ecommerce.order.domain.entity.Order;

public interface KafkaProducerService {
    void sendOrderCreatedEvent(Order order);
}