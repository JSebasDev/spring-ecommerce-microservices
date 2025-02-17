package com.ecommerce.product.service.impl;

import com.ecommerce.product.domain.entity.Category;
import com.ecommerce.product.dto.request.ProductRequest;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.exception.NotFoundException;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();


        testCategory = Category.builder()
                .name("Test Category")
                .description("A test category")
                .active(true)
                .build();
        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void testCreateProduct() {
        ProductRequest request = ProductRequest.builder()
                .name("Test Product")
                .description("Test product description")
                .price(BigDecimal.valueOf(10.00))
                .stock(50)
                .categoryId(testCategory.getId())
                .build();

        ProductResponse response = productService.createProduct(request);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals("Test product description", response.getDescription());
        assertEquals(BigDecimal.valueOf(10.00), response.getPrice());
        assertEquals(50, response.getStock());
        assertTrue(response.isActive());
        assertNotNull(response.getCategory());
        assertEquals(testCategory.getId(), response.getCategory().getId());
    }

    @Test
    void testGetProductById_NotFound() {
        String invalidId = "nonexistentId";
        Exception exception = assertThrows(NotFoundException.class, () -> {
            productService.getProductById(invalidId);
        });
        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateProduct() {
        ProductRequest createRequest = ProductRequest.builder()
                .name("Original Product")
                .description("Original description")
                .price(BigDecimal.valueOf(20.00))
                .stock(30)
                .categoryId(testCategory.getId())
                .build();
        ProductResponse createdProduct = productService.createProduct(createRequest);
        assertNotNull(createdProduct);

        ProductRequest updateRequest = ProductRequest.builder()
                .name("Updated Product")
                .description("Updated description")
                .price(BigDecimal.valueOf(25.00))
                .stock(40)
                .categoryId(testCategory.getId())
                .build();
        ProductResponse updatedProduct = productService.updateProduct(createdProduct.getId(), updateRequest);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated description", updatedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(25.00), updatedProduct.getPrice());
        assertEquals(40, updatedProduct.getStock());
    }

    @Test
    void testGetAllProducts() {
        for (int i = 1; i <= 5; i++) {
            ProductRequest request = ProductRequest.builder()
                    .name("Product " + i)
                    .description("Description " + i)
                    .price(BigDecimal.valueOf(10 * i))
                    .stock(10 * i)
                    .categoryId(testCategory.getId())
                    .build();
            productService.createProduct(request);
        }

        Page<ProductResponse> productPage = productService.getAllProducts(PageRequest.of(0, 10));
        assertNotNull(productPage);
        assertTrue(productPage.getTotalElements() >= 5);
    }

    @Test
    void testDeleteProduct() {
        ProductRequest request = ProductRequest.builder()
                .name("Product to Delete")
                .description("Will be deleted")
                .price(BigDecimal.valueOf(15.00))
                .stock(20)
                .categoryId(testCategory.getId())
                .build();
        ProductResponse createdProduct = productService.createProduct(request);
        assertNotNull(createdProduct);
        assertTrue(createdProduct.isActive());

        productService.deleteProduct(createdProduct.getId());
        ProductResponse deletedProduct = productService.getProductById(createdProduct.getId());
        assertFalse(deletedProduct.isActive());
    }
}
