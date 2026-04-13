package com.ecommerce.service;

import com.ecommerce.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.repository.ProductRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public void updateProduct(UUID productId, int stock) {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        product.get().setStock(product.get().getStock() - stock);
        productRepository.save(product.get());
    }
}
