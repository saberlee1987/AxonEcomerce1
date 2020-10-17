package com.saber.ecom.product.controllers;

import com.saber.ecom.product.model.Product;
import com.saber.ecom.product.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
@Slf4j
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(name = "id") String id, @RequestBody Product product) {
        log.debug("Updating product  with id ====> {}", id);
        Optional<Product> currentProductOptional = productRepository.findById(id);
        if (!currentProductOptional.isPresent()) {
            log.debug("Product with id ====> {} not found", id);
            return ResponseEntity.notFound().build();
        }
        Product currentProduct = currentProductOptional.get();
        currentProduct.setCode(product.getCode());
        currentProduct.setDescription(product.getDescription());
        currentProduct.setImgUrl(product.getImgUrl());
        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setProductCategoryName(product.getProductCategoryName());
        currentProduct.setTitle(product.getTitle());
        Product productUpdated = productRepository.save(currentProduct);
        return ResponseEntity.ok(productUpdated);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        log.debug("Creating Product with Code ===> {} ", product.getCode());
        List<Product> products = productRepository.findByCode(product.getCode());
        if (products.size() > 0) {
            log.debug("A Product with Code {} Already Exist", product.getCode());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Product productSaved = productRepository.save(product);
        return ResponseEntity.ok(productSaved);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") String id) {
        log.debug("Fetching Product with id ===> {}", id);
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            log.debug("Product With id ===> {} Not Found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productOptional.get());
    }

    @GetMapping(params = "category")
    public ResponseEntity<List<Product>> getProductWithCategoryName(@RequestParam(name = "category") String category) {
        log.debug("Find Product With Category ===> {}", category);
        List<Product> products = this.productRepository.findByProductCategoryName(category);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable(name = "id") String id) {
        log.debug("Fetching Product With id ==> {} ", id);
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            log.debug("Product with id ==> {} No Found", id);
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(productOptional.get());
        return ResponseEntity.noContent().build();
    }
}
