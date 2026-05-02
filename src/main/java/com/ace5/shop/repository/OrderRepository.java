package com.ace5.shop.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ace5.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

	@EntityGraph(attributePaths = { "items", "items.product" })
	Page<Order> findDistinctByUserIdAndDeletedFalse(UUID userId, Pageable pageable);

	@EntityGraph(attributePaths = { "items", "items.product" })
	Optional<Order> findByIdAndUserIdAndDeletedFalse(UUID id, UUID userId);
}
