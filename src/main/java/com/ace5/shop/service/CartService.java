package com.ace5.shop.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ace5.shop.entity.Cart;
import com.ace5.shop.entity.CartItem;
import com.ace5.shop.entity.Product;
import com.ace5.shop.entity.User;
import com.ace5.shop.repository.CartItemRepository;
import com.ace5.shop.repository.CartRepository;
import com.ace5.shop.repository.ProductRepository;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final CurrentUserService currentUserService;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			ProductRepository productRepository, CurrentUserService currentUserService) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.productRepository = productRepository;
		this.currentUserService = currentUserService;
	}

	@Transactional
	public Cart getCurrentUserCart() {
		User user = currentUserService.getCurrentUser();
		return findOrCreateCart(user);
	}

	@Transactional
	public Cart addItem(UUID productId, Integer quantity) {
		User user = currentUserService.getCurrentUser();
		Cart cart = findOrCreateCart(user);
		Product product = findActiveProduct(productId);

		validateStock(product, quantity);

		Optional<CartItem> existingItem = cart.getItems().stream()
				.filter(item -> !item.isDeleted())
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst();

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			int newQuantity = item.getQuantity() + quantity;
			validateStock(product, newQuantity);
			item.setQuantity(newQuantity);
		} else {
			CartItem item = new CartItem();
			item.setCart(cart);
			item.setProduct(product);
			item.setQuantity(quantity);
			cart.getItems().add(item);
		}

		return cartRepository.save(cart);
	}

	@Transactional
	public Cart updateItemQuantity(UUID itemId, Integer quantity) {
		User user = currentUserService.getCurrentUser();
		CartItem item = findCurrentUserCartItem(itemId, user);

		validateStock(item.getProduct(), quantity);
		item.setQuantity(quantity);

		cartItemRepository.save(item);
		return findOrCreateCart(user);
	}

	@Transactional
	public Cart removeItem(UUID itemId) {
		User user = currentUserService.getCurrentUser();
		CartItem item = findCurrentUserCartItem(itemId, user);

		item.setDeleted(true);
		cartItemRepository.save(item);

		return findOrCreateCart(user);
	}

	@Transactional
	public void clearCurrentUserCart() {
		Cart cart = getCurrentUserCart();
		cart.getItems().forEach(item -> item.setDeleted(true));
		cartRepository.save(cart);
	}

	private Cart findOrCreateCart(User user) {
		return cartRepository.findByUserIdAndDeletedFalse(user.getId())
				.orElseGet(() -> {
					Cart cart = new Cart();
					cart.setUser(user);
					return cartRepository.save(cart);
				});
	}

	private CartItem findCurrentUserCartItem(UUID itemId, User user) {
		return cartItemRepository.findByIdAndCartUserIdAndCartDeletedFalseAndDeletedFalse(itemId, user.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found."));
	}

	private Product findActiveProduct(UUID productId) {
		return productRepository.findByIdAndDeletedFalse(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
	}

	private void validateStock(Product product, Integer quantity) {
		if (product.getStockQuantity() < quantity) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient product stock.");
		}
	}
}
