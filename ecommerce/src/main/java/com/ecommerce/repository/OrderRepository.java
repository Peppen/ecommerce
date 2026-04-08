package com.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.entity.Order;

import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAll(Pageable pageable);

}
