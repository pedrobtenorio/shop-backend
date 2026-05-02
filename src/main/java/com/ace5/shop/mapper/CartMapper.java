package com.ace5.shop.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ace5.shop.dto.CartItemResponse;
import com.ace5.shop.dto.CartResponse;
import com.ace5.shop.entity.Cart;
import com.ace5.shop.entity.CartItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CartMapper {

	@Mapping(target = "total", expression = "java(calculateTotal(cart.getItems()))")
	@Mapping(target = "items", expression = "java(toActiveItemResponseList(cart.getItems()))")
	CartResponse toResponse(Cart cart);

	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "productName", source = "product.name")
	@Mapping(target = "unitPrice", source = "product.price")
	@Mapping(target = "subtotal", expression = "java(calculateSubtotal(item.getProduct().getPrice(), item.getQuantity()))")
	CartItemResponse toItemResponse(CartItem item);

	List<CartItemResponse> toItemResponseList(List<CartItem> items);

	default List<CartItemResponse> toActiveItemResponseList(List<CartItem> items) {
		return items.stream()
				.filter(item -> !item.isDeleted())
				.map(this::toItemResponse)
				.toList();
	}

	default BigDecimal calculateTotal(List<CartItem> items) {
		return items.stream()
				.filter(item -> !item.isDeleted())
				.map(item -> calculateSubtotal(item.getProduct().getPrice(), item.getQuantity()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	default BigDecimal calculateSubtotal(BigDecimal unitPrice, Integer quantity) {
		return unitPrice.multiply(BigDecimal.valueOf(quantity));
	}
}
