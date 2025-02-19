package com.ecommerce.order.repository;

import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCustomerId(String customerId);
    List<Order> findByStatus(OrderStatus status);
}