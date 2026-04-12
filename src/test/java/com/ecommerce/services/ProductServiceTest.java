package com.ecommerce.services;

import com.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.ConflictException;
import com.ecommerce.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService sut;

    @Test
    void findAllShouldReturnPagedProducts() {
        Pageable pageable = PageRequest.of(0, 5);

        List<Product> products = List.of(
                new Product("CODE1", "Prodotto 1", 10),
                new Product("CODE2", "Prodotto 2", 20)
        );

        Page<Product> page = new PageImpl<>(products, pageable, products.size());

        Mockito.when(productRepository.findAll(pageable)).thenReturn(page);

        Page<Product> result = sut.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Prodotto 1", result.getContent().getFirst().getName());
        Mockito.verify(productRepository).findAll(pageable);
    }

    @Test
    void createProductWhenCodeNotExists() {
        String code = "P123";
        Mockito.when(productRepository.findByCode(code)).thenReturn(Optional.empty());

        Product saved = new Product(code, "Mouse", 50);

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(saved);

        ProductDto result = sut.createProduct("Mouse", code, 50);

        assertEquals("Mouse", result.getName());
        assertEquals(code, result.getCode());
        assertEquals(50, result.getStock());

        Mockito.verify(productRepository).findByCode(code);
        Mockito.verify(productRepository).save(Mockito.any(Product.class));
    }

    @Test
    void createProductWhenProductAlreadyExists() {
        String code = "P999";
        Product existing = new Product(code, "Tastiera", 30);

        Mockito.when(productRepository.findByCode(code))
                .thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () ->
                sut.createProduct("Tastiera", code, 30)
        );

        Mockito.verify(productRepository).findByCode(code);
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any());
    }
}
