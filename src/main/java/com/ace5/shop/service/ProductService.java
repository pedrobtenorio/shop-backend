package com.ace5.shop.service;

import com.ace5.shop.entity.Product;
import com.ace5.shop.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {
    final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return findActiveProduct(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Product product) {
        product = findActiveProduct(product.getId());
        return productRepository.save(product);
    }

    @Transactional
    public Product deleteProduct(UUID id) {
        Product product = findActiveProduct(id);
        product.setDeleted(true);
        return productRepository.save(product);
    }

    private Product findActiveProduct(UUID id) {
        return productRepository.findByIdAndDeletedFalse((id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
    }
}
