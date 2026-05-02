package com.ace5.shop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ace5.shop.dto.AuthResponse;
import com.ace5.shop.dto.LoginRequest;
import com.ace5.shop.dto.RegisterRequest;
import com.ace5.shop.service.UserAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserAuthService userAuthService;

	public AuthController(UserAuthService userAuthService) {
		this.userAuthService = userAuthService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return userAuthService.register(request);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return userAuthService.login(request);
	}
}
