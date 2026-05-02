package com.ace5.shop.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ace5.shop.entity.User;

@Service
public class CurrentUserService {

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found.");
		}

		return user;
	}
}
