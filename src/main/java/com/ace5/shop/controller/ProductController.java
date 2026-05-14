package com.ace5.shop.controller;


import com.ace5.shop.entity.Product;
import com.ace5.shop.repository.ProductRepository;
import com.ace5.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        return this.productService.createProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @PutMapping
    public Product updateProduct(@RequestBody Product product) {
        return this.productService.updateProduct(product);
    }

    @DeleteMapping
    public Product deleteProduct(@RequestBody UUID productId) {
        return this.productService.deleteProduct(productId);
    }

}
