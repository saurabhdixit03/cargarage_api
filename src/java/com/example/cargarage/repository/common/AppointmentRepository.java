package com.example.cargarage.repository.common;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cargarage.model.common.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomerId(Long customerId);

    // Custom query to fetch all appointments with full data
    @Query("SELECT a FROM Appointment a JOIN FETCH a.customer c JOIN FETCH a.car cr JOIN FETCH a.service s")
    List<Appointment> findAllWithDetails();
}
