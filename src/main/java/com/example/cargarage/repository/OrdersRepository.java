package com.example.cargarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cargarage.model.*;
public interface OrdersRepository extends JpaRepository<Orders, Integer>{
	

}
