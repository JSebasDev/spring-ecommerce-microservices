package com.ecommerce.product.controller;

import com.ecommerce.product.dto.request.ProductRequest;
import com.ecommerce.product.dto.response.ApiResponse;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.product.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseBuilder.build(
                        HttpStatus.CREATED,
                        "Product created successfully",
                        productService.createProduct(request)
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(ResponseBuilder.build(
                HttpStatus.OK,
                "Product updated successfully",
                productService.updateProduct(id, request)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(ResponseBuilder.build(
                HttpStatus.OK,
                "Product retrieved successfully",
                productService.getProductById(id)
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(ResponseBuilder.build(
                HttpStatus.OK,
                "Products retrieved successfully",
                productService.getAllProducts(pageable)
        ));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable String categoryId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ResponseBuilder.build(
                HttpStatus.OK,
                "Products retrieved successfully",
                productService.getProductsByCategory(categoryId, pageable)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ResponseBuilder.build(
                HttpStatus.OK,
                "Product deleted successfully",
                null
        ));
    }
}
