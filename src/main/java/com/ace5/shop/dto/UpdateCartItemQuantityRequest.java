package com.ace5.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemQuantityRequest(
		@NotNull(message = "Quantity is required.")
		@Positive(message = "Quantity must be greater than zero.")
		Integer quantity) {
}
