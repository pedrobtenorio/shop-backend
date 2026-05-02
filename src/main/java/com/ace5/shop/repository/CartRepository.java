package com.ace5.shop.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ace5.shop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {

	@EntityGraph(attributePaths = { "items", "items.product" })
	Optional<Cart> findByUserIdAndDeletedFalse(UUID userId);
}
