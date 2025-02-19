package com.ecommerce.product.repository;

import com.ecommerce.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
    List<Product> findByIdIn(List<String> ids);
}
