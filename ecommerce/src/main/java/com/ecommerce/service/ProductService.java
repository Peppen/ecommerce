package com.ecommerce.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.repository.ProductRepository;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public ProductDto createProduct(String name, String code, int stock) {
        Optional<Product> existentProduct = productRepository.findByCode(code);
        if(existentProduct.isPresent()) {
            throw new ConflictException("Product already present");
        }
        Product product = new Product(code, name, stock);
        Product saved = productRepository.save(product);
        return new ProductDto(saved.getUuid(), saved.getCode(), saved.getName(), saved.getStock(), saved.getVersion());
    }
}
