package com.ecommerce.order.service.impl;

import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.exception.KafkaProducerException;
import com.ecommerce.order.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendOrderCreatedEvent(Order order) {
        try {
            kafkaTemplate.send("order-created", order.getId(), order);
            log.info("Order created event sent for order: {}", order.getId());
        } catch (Exception e) {
            log.error("Error sending order created event: {}", e.getMessage());
            throw new KafkaProducerException("Failed to send order created event: " + e.getMessage());
        }
    }
}