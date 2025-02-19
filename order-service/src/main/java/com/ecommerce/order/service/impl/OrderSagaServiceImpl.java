package com.ecommerce.order.service.impl;

import com.ecommerce.order.client.CustomerFeignClient;
import com.ecommerce.order.client.ProductFeignClient;
import com.ecommerce.order.domain.entity.Order;
import com.ecommerce.order.dto.request.OrderItemRequest;
import com.ecommerce.order.dto.response.UserResponse;
import com.ecommerce.order.dto.request.ValidatedOrderRequest;
import com.ecommerce.order.dto.request.ProductValidationRequest;
import com.ecommerce.order.dto.response.ProductValidationResponse;
import com.ecommerce.order.dto.request.OrderRequest;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.exception.OrderValidationException;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.service.OrderSagaService;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderSagaServiceImpl implements OrderSagaService {

    private final CustomerFeignClient customerClient;
    private final ProductFeignClient productClient;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrderWithSaga(OrderRequest request) {
        try {
            log.info("Starting order creation saga for customer: {}", request.getCustomerId());

            UserResponse customerDetails = customerClient.getUserById(request.getCustomerId()).getDetails();
            log.info("Customer validated successfully: {}", customerDetails.getEmail());

            Map<String, ProductValidationResponse> validatedProducts = validateProducts(request);
            log.info("Products validated successfully. Count: {}", validatedProducts.size());

            ValidatedOrderRequest validatedOrder = createValidatedOrder(request, validatedProducts);
            log.info("Order validated with total amount: {}", validatedOrder.getTotalAmount());

            OrderResponse createdOrder = orderService.createOrder(validatedOrder);

            log.info("Order created successfully with ID: {}", createdOrder.getId());
            return createdOrder;

        } catch (Exception e) {
            log.error("Error in order creation saga: {}", e.getMessage());
            throw new OrderValidationException("Failed to create order: " + e.getMessage());
        }
    }

    private Map<String, ProductValidationResponse> validateProducts(OrderRequest request) {
        List<String> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .distinct()
                .collect(Collectors.toList());

        ProductValidationRequest productRequest = new ProductValidationRequest();
        productRequest.setProductIds(productIds);

        List<ProductValidationResponse> validatedProducts = productClient.validateProducts(productRequest).getDetails();
        validateProductsAvailability(validatedProducts, request);

        return validatedProducts.stream()
                .collect(Collectors.toMap(
                        ProductValidationResponse::getId,
                        product -> product
                ));
    }

    private void validateProductsAvailability(List<ProductValidationResponse> products, OrderRequest request) {
        Map<String, Integer> requestedQuantities = request.getItems().stream()
                .collect(Collectors.toMap(
                        OrderItemRequest::getProductId,
                        OrderItemRequest::getQuantity,
                        Integer::sum
                ));

        Map<String, ProductValidationResponse> productMap = products.stream()
                .collect(Collectors.toMap(
                        ProductValidationResponse::getId,
                        product -> product
                ));

        requestedQuantities.forEach((productId, quantity) -> {
            ProductValidationResponse product = productMap.get(productId);
            if (product == null) {
                throw new OrderValidationException("Product not found: " + productId);
            }

            if (!product.isActive()) {
                throw new OrderValidationException("Product " + product.getName() + " is not active");
            }

            if (quantity > product.getStock()) {
                throw new OrderValidationException(
                        "Insufficient stock for product " + product.getName() +
                                ". Requested: " + quantity +
                                ", Available: " + product.getStock()
                );
            }
        });
    }

    private ValidatedOrderRequest createValidatedOrder(OrderRequest request,
                                                       Map<String, ProductValidationResponse> validatedProducts) {
        ValidatedOrderRequest validatedOrder = orderMapper.toValidatedRequest(request, validatedProducts);

        BigDecimal totalAmount = validatedOrder.getItems().stream()
                .map(ValidatedOrderRequest.ValidatedOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        validatedOrder.setTotalAmount(totalAmount);
        return validatedOrder;
    }
}