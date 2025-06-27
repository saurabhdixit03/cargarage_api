package com.example.cargarage.dto;

import java.util.List;

import com.example.cargarage.dto.CarDTO;
import com.example.cargarage.dto.AdminAppointmentRequestDTO;


public class CustomerWithDetailsDTO {
    private Long customerId;
    private String name;
    private String mobile;
    private String email;
    private String address;

    private List<CarDTO> cars;  // List of cars
    private List<AdminAppointmentRequestDTO> appointments;  // List of appointments

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public List<CarDTO> getCars() {
        return cars;
    }
    public void setCars(List<CarDTO> cars) {
        this.cars = cars;
    }
    public List<AdminAppointmentRequestDTO> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<AdminAppointmentRequestDTO> appointments) {
        this.appointments = appointments;
    }
}

