package com.ecommerce.product.controller;

import com.ecommerce.product.dto.request.ProductValidationRequest;
import com.ecommerce.product.dto.response.ApiResponse;
import com.ecommerce.product.dto.response.ProductValidationResponse;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.product.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductValidationController {

    private final ProductService productService;

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validateProducts(
            @RequestBody ProductValidationRequest request) {
        List<ProductValidationResponse> products = productService.getProductsByIds(request.getProductIds());

        ApiResponse response = ResponseBuilder.build(
                HttpStatus.OK,
                "Products validated successfully",
                products);

        return ResponseEntity.ok(response);
    }
}
