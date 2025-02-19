package com.ecommerce.product.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product extends BaseEntity {
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("price")
    private BigDecimal price;

    @Field("stock")
    private Integer stock;

    @Field("active")
    private boolean active;

    @DBRef
    @Field("category")
    private Category category;
}
