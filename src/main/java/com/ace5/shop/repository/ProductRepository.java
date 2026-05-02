package com.ace5.shop.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ace5.shop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	Page<Product> findAllByDeletedFalse(Pageable pageable);

	Optional<Product> findByIdAndDeletedFalse(UUID id);
}
