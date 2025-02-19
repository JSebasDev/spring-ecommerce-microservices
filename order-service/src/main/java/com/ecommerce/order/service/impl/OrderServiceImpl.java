package com.ecommerce.order.service.impl;

import com.ecommerce.order.domain.enums.OrderStatus;
import com.ecommerce.order.dto.request.ValidatedOrderRequest;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.KafkaProducerService;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaProducerService kafkaProducerService;

    @Override
    @Transactional
    public OrderResponse createOrder(ValidatedOrderRequest validatedOrder) {
        Order order = orderMapper.toEntity(validatedOrder);
        Order savedOrder = orderRepository.save(order);
        kafkaProducerService.sendOrderCreatedEvent(savedOrder);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        log.debug("Found order with ID: {}. Items count: {}", order.getId(),
                order.getItems() != null ? order.getItems().size() : 0);

        OrderResponse response = orderMapper.toResponse(order);

        log.debug("Mapped to response. Items count: {}",
                response.getItems() != null ? response.getItems().size() : 0);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getCustomerOrders(String customerId) {
        List<Order> customerOrders = orderRepository.findByCustomerIdWithItems(customerId);
        return customerOrders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel a delivered order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        log.info("Order cancelled successfully with ID: {}", orderId);
    }
}