package com.ace5.shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ace5.shop.entity.enums.OrderStatus;

public record OrderResponse(
		UUID id,
		OrderStatus status,
		LocalDateTime createdAt,
		List<OrderItemResponse> items,
		BigDecimal total) {
}
