package com.ace5.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.ace5.shop.dto.AuthResponse;
import com.ace5.shop.dto.RegisterRequest;
import com.ace5.shop.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "email", expression = "java(normalizeEmail(request.email()))")
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "role", constant = "USER")
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	User toEntity(RegisterRequest request);

	@Mapping(target = "token", source = "token")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "name", source = "user.name")
	@Mapping(target = "email", source = "user.email")
	@Mapping(target = "role", source = "user.role")
	AuthResponse toAuthResponse(User user, String token);

	default String normalizeEmail(String email) {
		return email.trim().toLowerCase();
	}
}
