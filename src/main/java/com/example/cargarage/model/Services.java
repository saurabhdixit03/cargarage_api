package com.example.cargarage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// JPA entity representing a service offered in the garage
@Entity
public class Services {

    // Primary key with auto-generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for each service

    private String name;                 // Name of the service (e.g., Oil Change)
    private String description;          // Description of what the service includes
    private Double budget;               // Cost estimate or budget for this service
    private Double discount_percentage;  // Discount applied on this service (if any)

    // Default constructor (required by JPA)
    public Services() {}

    // Parameterized constructor to initialize all fields
    public Services(Long id, String name, String description, Double budget, Double discount_percentage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.discount_percentage = discount_percentage;
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getDiscountPercentage() {
        return discount_percentage;
    }

    public void setDiscountPercentage(Double discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    // toString method for logging or debugging purposes
    @Override
    public String toString() {
        return "Services{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", budget=" + budget +
               ", discount_percentage=" + discount_percentage +
               '}';
    }
}
