package com.ace5.shop.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ace5.shop.entity.Cart;
import com.ace5.shop.entity.CartItem;
import com.ace5.shop.entity.Order;
import com.ace5.shop.entity.OrderItem;
import com.ace5.shop.entity.Product;
import com.ace5.shop.entity.User;
import com.ace5.shop.repository.CartRepository;
import com.ace5.shop.repository.OrderRepository;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final CurrentUserService currentUserService;
	private final CartService cartService;

	public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
			CurrentUserService currentUserService, CartService cartService) {
		this.orderRepository = orderRepository;
		this.cartRepository = cartRepository;
		this.currentUserService = currentUserService;
		this.cartService = cartService;
	}

	@Transactional
	public Order createOrderFromCart() {
		User user = currentUserService.getCurrentUser();
		Cart cart = cartService.getCurrentUserCart();
		List<CartItem> activeItems = cart.getItems().stream()
				.filter(item -> !item.isDeleted())
				.toList();

		if (activeItems.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty.");
		}

		Order order = new Order();
		order.setUser(user);

		for (CartItem cartItem : activeItems) {
			Product product = cartItem.getProduct();
			Integer quantity = cartItem.getQuantity();

			if (product.isDeleted()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product " + product.getName() + " is no longer available.");
			}

			if (product.getStockQuantity() < quantity) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for " + product.getName() + ".");
			}

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(quantity);
			orderItem.setUnitPrice(product.getPrice());
			order.getItems().add(orderItem);

			product.setStockQuantity(product.getStockQuantity() - quantity);
			cartItem.setDeleted(true);
		}

		cartRepository.save(cart);
		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public Page<Order> findCurrentUserOrders(Pageable pageable) {
		User user = currentUserService.getCurrentUser();
		return orderRepository.findDistinctByUserIdAndDeletedFalse(user.getId(), pageable);
	}

	@Transactional(readOnly = true)
	public Order findCurrentUserOrderById(UUID id) {
		User user = currentUserService.getCurrentUser();
		return orderRepository.findByIdAndUserIdAndDeletedFalse(id, user.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
	}
}
