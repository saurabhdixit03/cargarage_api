package com.example.cargarage.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Car {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;   // e.g., Toyota, Honda
    private String model;  // e.g., Corolla, Civic
    private String color;
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore			 // This will prevent infinite recursion	
    private Customer customer;  // Relationship with Customer

    
    
    // Constructors
    public Car() {}

    public Car(Long id, String make, String model, String color, String license_plate, Customer customer) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.color = color;
        this.licensePlate = license_plate;
        this.customer = customer;
    }

    
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String license_plate) {
        this.licensePlate = license_plate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}