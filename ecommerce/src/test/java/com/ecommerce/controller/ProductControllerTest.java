package com.ecommerce.controller;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPagedProducts() throws Exception {
        Product product = new Product("PRD001", "Laptop", 12);

        Page<Product> page = new PageImpl<>(List.of(product));

        Mockito.when(productService.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        // WHEN + THEN
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDtoList[0].code").value("PRD001"))
                .andExpect(jsonPath("$.productDtoList[0].name").value("Laptop"))
                .andExpect(jsonPath("$.productDtoList[0].stock").value(12));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        // GIVEN
        ProductDto inputDto = new ProductDto(
                UUID.randomUUID(),
                "PRD001",
                "Laptop",
                12,
                0
        );

        ProductDto outputDto = new ProductDto(
                UUID.randomUUID(),
                "PRD001",
                "Laptop",
                12,
                0
        );

        Mockito.when(productService.createProduct(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt()
        )).thenReturn(outputDto);

        // WHEN + THEN
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRD001"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.stock").value(12));
    }
}
