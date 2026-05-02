package com.ace5.shop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
		UUID id,
		UUID productId,
		String productName,
		BigDecimal unitPrice,
		Integer quantity,
		BigDecimal subtotal) {
}
