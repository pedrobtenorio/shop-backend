package com.ace5.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockRequest(
		@NotNull(message = "Stock quantity is required.")
		@PositiveOrZero(message = "Stock quantity must be zero or greater.")
		Integer stockQuantity) {
}
