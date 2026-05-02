package com.ace5.shop.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ace5.shop.dto.ProductRequest;
import com.ace5.shop.dto.ProductResponse;
import com.ace5.shop.dto.StockRequest;
import com.ace5.shop.entity.Product;
import com.ace5.shop.mapper.ProductMapper;
import com.ace5.shop.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final ProductMapper productMapper;

	public ProductController(ProductService productService, ProductMapper productMapper) {
		this.productService = productService;
		this.productMapper = productMapper;
	}

	@GetMapping
	public Page<ProductResponse> findAll(Pageable pageable) {
		return productService.findAll(pageable)
				.map(productMapper::toResponse);
	}

	@GetMapping("/{id}")
	public ProductResponse findById(@PathVariable UUID id) {
		return productMapper.toResponse(productService.findById(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse create(@Valid @RequestBody ProductRequest request) {
		Product product = productService.create(request);

		return productMapper.toResponse(product);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
		Product product = productService.update(id, request);

		return productMapper.toResponse(product);
	}

	@PatchMapping("/{id}/stock")
	public ProductResponse updateStock(@PathVariable UUID id, @Valid @RequestBody StockRequest request) {
		return productMapper.toResponse(productService.updateStock(id, request.stockQuantity()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		productService.delete(id);
	}
}
