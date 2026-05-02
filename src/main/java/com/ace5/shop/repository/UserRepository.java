package com.ace5.shop.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ace5.shop.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsByEmailAndDeletedFalse(String email);

	Optional<User> findByEmailAndDeletedFalse(String email);
}
