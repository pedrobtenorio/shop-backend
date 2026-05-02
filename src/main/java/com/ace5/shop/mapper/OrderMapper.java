package com.ace5.shop.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ace5.shop.dto.OrderItemResponse;
import com.ace5.shop.dto.OrderResponse;
import com.ace5.shop.entity.Order;
import com.ace5.shop.entity.OrderItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {

	@Mapping(target = "total", expression = "java(calculateTotal(order.getItems()))")
	@Mapping(target = "items", expression = "java(toActiveItemResponseList(order.getItems()))")
	OrderResponse toResponse(Order order);

	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "productName", source = "product.name")
	@Mapping(target = "subtotal", expression = "java(calculateSubtotal(item.getUnitPrice(), item.getQuantity()))")
	OrderItemResponse toItemResponse(OrderItem item);

	List<OrderItemResponse> toItemResponseList(List<OrderItem> items);

	default List<OrderItemResponse> toActiveItemResponseList(List<OrderItem> items) {
		return items.stream()
				.filter(item -> !item.isDeleted())
				.map(this::toItemResponse)
				.toList();
	}

	default BigDecimal calculateTotal(List<OrderItem> items) {
		return items.stream()
				.filter(item -> !item.isDeleted())
				.map(item -> calculateSubtotal(item.getUnitPrice(), item.getQuantity()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	default BigDecimal calculateSubtotal(BigDecimal unitPrice, Integer quantity) {
		return unitPrice.multiply(BigDecimal.valueOf(quantity));
	}
}
