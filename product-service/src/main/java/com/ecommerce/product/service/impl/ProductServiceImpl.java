package com.ecommerce.product.service.impl;

import com.ecommerce.product.domain.entity.Category;
import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.dto.request.ProductRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.dto.response.ProductValidationResponse;
import com.ecommerce.product.exception.NotFoundException;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setActive(true);

        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        productMapper.updateEntity(request, product);
        product.setCategory(category);

        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByActiveTrue(pageable);
        return products.map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);
        return products.map(productMapper::toResponse);
    }

    @Override
    public List<ProductValidationResponse> getProductsByIds(List<String> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);

        if (products.size() != productIds.size()) {
            List<String> foundIds = products.stream().map(Product::getId).toList();
            List<String> missingIds = productIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new ResourceNotFoundException("Products not found for IDs: " + missingIds);
        }

        return products.stream()
                .map(product -> ProductValidationResponse.builder()
                        .id(product.getId())
                        .active(product.isActive())
                        .stock(product.getStock())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }
}
