package com.ecommerce.product.mapper;

import com.ecommerce.product.domain.entity.Specification;
import com.ecommerce.product.dto.request.SpecificationRequest;
import com.ecommerce.product.dto.response.SpecificationResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecificationMapper {

    Specification toEntity(SpecificationRequest request);

    SpecificationResponse toResponse(Specification specification);

    List<Specification> toEntityList(List<SpecificationRequest> requests);

    List<SpecificationResponse> toResponseList(List<Specification> specifications);
}
