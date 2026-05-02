package com.ace5.shop.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest(
		@NotBlank(message = "Product name is required.")
		String name,
		String description,
		@NotNull(message = "Product price is required.")
		@PositiveOrZero(message = "Product price must be zero or greater.")
		BigDecimal price,
		@NotNull(message = "Stock quantity is required.")
		@PositiveOrZero(message = "Stock quantity must be zero or greater.")
		Integer stockQuantity) {
}
