package com.ace5.shop.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ace5.shop.dto.ProductRequest;
import com.ace5.shop.entity.Product;
import com.ace5.shop.mapper.ProductMapper;
import com.ace5.shop.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	@Transactional(readOnly = true)
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAllByDeletedFalse(pageable);
	}

	@Transactional(readOnly = true)
	public Product findById(UUID id) {
		return findActiveProduct(id);
	}

	@Transactional
	public Product create(ProductRequest request) {
		Product product = productMapper.toEntity(request);
		product.setName(request.name().trim());

		return productRepository.save(product);
	}

	@Transactional
	public Product update(UUID id, ProductRequest request) {
		Product product = findActiveProduct(id);
		productMapper.updateEntity(request, product);
		product.setName(request.name().trim());

		return productRepository.save(product);
	}

	@Transactional
	public Product updateStock(UUID id, Integer stockQuantity) {
		if (stockQuantity == null || stockQuantity < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock quantity must be zero or greater.");
		}

		Product product = findActiveProduct(id);
		product.setStockQuantity(stockQuantity);

		return productRepository.save(product);
	}

	@Transactional
	public void delete(UUID id) {
		Product product = findActiveProduct(id);
		product.setDeleted(true);
		productRepository.save(product);
	}

	private Product findActiveProduct(UUID id) {
		return productRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
	}
}
