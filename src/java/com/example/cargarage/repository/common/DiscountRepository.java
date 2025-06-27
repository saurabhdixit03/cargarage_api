package com.example.cargarage.repository.common;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.common.Discount;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByCustomerId(Long customerId);
    List<Discount> findByCustomerIdIsNull();
}
