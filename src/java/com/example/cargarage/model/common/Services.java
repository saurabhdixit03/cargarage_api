package com.example.cargarage.model.common;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for each service

    private String name;  // Name of the service
    private String description;  // Description of the service
    private Double budget;  // Budget or cost for the service
    private Double discount_percentage;  // Discount percentage on the service (optional)

    // Default constructor
    public Services() {}

    // Constructor to initialize all fields
    public Services(Long id, String name, String description, Double budget, Double discount_percentage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.budget = budget;
        this.discount_percentage = discount_percentage;
    }

    // Getters and setters

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

    // Override toString() for debugging and display
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
