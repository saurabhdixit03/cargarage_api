package com.example.cargarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.Customer;
import com.example.cargarage.model.FaceliftKitBooking;

import java.util.List;

public interface FaceliftKitBookingRepository extends JpaRepository<FaceliftKitBooking, Long> {
    List<FaceliftKitBooking> findByCustomer(Customer customer);
}
