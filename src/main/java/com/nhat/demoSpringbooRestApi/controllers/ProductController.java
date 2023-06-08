package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.configs.AppConstants;
import com.nhat.demoSpringbooRestApi.dtos.ProductListResponseDTO;
import com.nhat.demoSpringbooRestApi.dtos.ProductRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ProductListResponseDTO> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductListResponseDTO productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<ProductListResponseDTO>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductRequestDTO product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId, @Valid @RequestBody ProductRequestDTO product) {
        Product savedProduct = productService.updateProduct(productId, product);
        return new ResponseEntity<Product>(savedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        String message = productService.deleteProduct(productId);
        return new ResponseEntity<String>(message, HttpStatus.OK);
    }


}
