package com.ecommerce.product.service;

import com.ecommerce.product.dto.request.ProductRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.dto.response.ProductValidationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(String id, ProductRequest request);
    ProductResponse getProductById(String id);
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable);
    List<ProductValidationResponse> getProductsByIds(List<String> productIds);
    void deleteProduct(String id);
}
