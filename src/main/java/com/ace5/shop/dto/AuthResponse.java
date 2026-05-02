package com.ace5.shop.dto;

import java.util.UUID;

import com.ace5.shop.entity.enums.UserRole;

public record AuthResponse(
		String token,
		UUID userId,
		String name,
		String email,
		UserRole role) {
}
