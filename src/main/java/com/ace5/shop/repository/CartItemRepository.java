package com.ace5.shop.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ace5.shop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

	@EntityGraph(attributePaths = { "cart", "cart.user", "product" })
	Optional<CartItem> findByIdAndCartUserIdAndCartDeletedFalseAndDeletedFalse(UUID id, UUID userId);
}
