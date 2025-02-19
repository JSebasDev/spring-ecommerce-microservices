package com.ecommerce.order.service;

import com.ecommerce.order.dto.request.OrderRequest;
import com.ecommerce.order.dto.request.ValidatedOrderRequest;
import com.ecommerce.order.dto.response.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(ValidatedOrderRequest validatedOrder);
    OrderResponse getOrderById(String orderId);
    List<OrderResponse> getCustomerOrders(String customerId);
    void cancelOrder(String orderId);
}