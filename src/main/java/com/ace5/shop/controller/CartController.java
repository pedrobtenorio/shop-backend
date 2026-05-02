package com.ace5.shop.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ace5.shop.dto.AddCartItemRequest;
import com.ace5.shop.dto.CartResponse;
import com.ace5.shop.dto.UpdateCartItemQuantityRequest;
import com.ace5.shop.mapper.CartMapper;
import com.ace5.shop.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;
	private final CartMapper cartMapper;

	public CartController(CartService cartService, CartMapper cartMapper) {
		this.cartService = cartService;
		this.cartMapper = cartMapper;
	}

	@GetMapping
	public CartResponse getCart() {
		return cartMapper.toResponse(cartService.getCurrentUserCart());
	}

	@PostMapping("/items")
	@ResponseStatus(HttpStatus.CREATED)
	public CartResponse addItem(@Valid @RequestBody AddCartItemRequest request) {
		return cartMapper.toResponse(cartService.addItem(request.productId(), request.quantity()));
	}

	@PatchMapping("/items/{itemId}")
	public CartResponse updateItemQuantity(@PathVariable UUID itemId,
			@Valid @RequestBody UpdateCartItemQuantityRequest request) {
		return cartMapper.toResponse(cartService.updateItemQuantity(itemId, request.quantity()));
	}

	@DeleteMapping("/items/{itemId}")
	public CartResponse removeItem(@PathVariable UUID itemId) {
		return cartMapper.toResponse(cartService.removeItem(itemId));
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void clearCart() {
		cartService.clearCurrentUserCart();
	}
}
