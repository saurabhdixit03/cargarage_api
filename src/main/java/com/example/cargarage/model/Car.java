package com.example.cargarage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// This entity represents a Car owned by a customer.
// Each car has basic details and is linked to one customer.
@Entity
public class Car {

    // Primary key for the car
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic attributes of the car
    private String make;           // e.g., Toyota, Honda
    private String model;          // e.g., Corolla, Civic
    private String color;
    private String licensePlate;

    // Many cars can belong to one customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore // Prevents infinite loop during JSON serialization
    private Customer customer;

    // Default constructor for JPA
    public Car() {}

    // Constructor to create a Car with all fields
    public Car(Long id, String make, String model, String color, String licensePlate, Customer customer) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.color = color;
        this.licensePlate = licensePlate;
        this.customer = customer;
    }

    // Getters and setters
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

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
