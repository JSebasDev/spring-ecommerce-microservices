package com.ecommerce.order.controller;

import com.ecommerce.order.dto.request.OrderRequest;
import com.ecommerce.order.dto.response.ApiResponse;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.service.OrderSagaService;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.order.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderSagaService orderSagaService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        log.info("Creating order for customer: {}", request.getCustomerId());
        OrderResponse order = orderSagaService.createOrderWithSaga(request);

        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Order created successfully")
                .details(order)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @PathVariable String orderId
    ) {
        log.info("Fetching order details for ID: {}", orderId);
        OrderResponse order = orderService.getOrderById(orderId);

        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Order retrieved successfully")
                .details(order)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getCustomerOrders(
            @PathVariable String customerId
    ) {
        log.info("Fetching orders for customer: {}", customerId);
        List<OrderResponse> orders = orderService.getCustomerOrders(customerId);

        ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Customer orders retrieved successfully")
                .details(orders)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable String orderId
    ) {
        log.info("Cancelling order with ID: {}", orderId);
        orderService.cancelOrder(orderId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Order cancelled successfully")
                .build();

        return ResponseEntity.ok(response);
    }
}