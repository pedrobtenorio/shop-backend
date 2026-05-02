package com.ace5.shop.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ace5.shop.dto.AuthResponse;
import com.ace5.shop.dto.LoginRequest;
import com.ace5.shop.dto.RegisterRequest;
import com.ace5.shop.entity.User;
import com.ace5.shop.mapper.UserMapper;
import com.ace5.shop.repository.UserRepository;

@Service
public class UserAuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserMapper userMapper;

	public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userMapper = userMapper;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		String email = request.email().trim().toLowerCase();

		if (userRepository.existsByEmailAndDeletedFalse(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered.");
		}

		User user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.password()));

		User savedUser = userRepository.save(user);
		return buildAuthResponse(savedUser);
	}

	public AuthResponse login(LoginRequest request) {
		String email = request.email().trim().toLowerCase();

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, request.password()));

		User user = userRepository.findByEmailAndDeletedFalse(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials."));

		return buildAuthResponse(user);
	}

	private AuthResponse buildAuthResponse(User user) {
		String token = jwtService.generateToken(user);
		return userMapper.toAuthResponse(user, token);
	}
}
