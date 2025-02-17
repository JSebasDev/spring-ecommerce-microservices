package com.ecommerce.product.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@Document(collection = "categories")
public class Category extends BaseEntity {

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("active")
    private boolean active;
}
