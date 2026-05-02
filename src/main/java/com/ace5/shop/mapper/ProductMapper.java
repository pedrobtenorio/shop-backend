package com.ace5.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ace5.shop.dto.ProductRequest;
import com.ace5.shop.dto.ProductResponse;
import com.ace5.shop.entity.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	Product toEntity(ProductRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	void updateEntity(ProductRequest request, @MappingTarget Product product);

	ProductResponse toResponse(Product product);
}
