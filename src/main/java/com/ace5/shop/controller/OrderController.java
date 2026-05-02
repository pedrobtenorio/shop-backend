package com.ace5.shop.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ace5.shop.dto.OrderResponse;
import com.ace5.shop.mapper.OrderMapper;
import com.ace5.shop.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;
	private final OrderMapper orderMapper;

	public OrderController(OrderService orderService, OrderMapper orderMapper) {
		this.orderService = orderService;
		this.orderMapper = orderMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse createOrder() {
		return orderMapper.toResponse(orderService.createOrderFromCart());
	}

	@GetMapping
	public Page<OrderResponse> findOrders(Pageable pageable) {
		return orderService.findCurrentUserOrders(pageable)
				.map(orderMapper::toResponse);
	}

	@GetMapping("/{id}")
	public OrderResponse findOrderById(@PathVariable UUID id) {
		return orderMapper.toResponse(orderService.findCurrentUserOrderById(id));
	}
}
