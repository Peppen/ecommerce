package com.ecommerce.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.ProductResponseDto;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductResponseDto findAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findAll(pageable);
        List<ProductDto> productDtoList = products.stream().map(p -> new ProductDto(p.getUuid(), p.getCode(), p.getName(), p.getStock(), p.getVersion())).toList();
        return new ProductResponseDto(productDtoList);
    }

    @PostMapping
    public ProductDto create(@RequestBody Product product) {
        return productService.createProduct(product.getName(), product.getCode(), product.getStock());
    }

}
