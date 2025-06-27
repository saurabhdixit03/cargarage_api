package com.example.cargarage.model.common;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "faceliftkits")
public class FaceliftKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carModel;   // Fortuner, XUV700, etc.
    private String kitName;    // Name of the kit
    private String description;
    private Double price;
    private String imageUrl;
    private boolean isActive = true;

    // Default Constructor
    public FaceliftKit() {
    }

    // Parameterized Constructor
    public FaceliftKit(Long id, String carModel, String kitName, String description, Double price, String imageUrl, boolean isActive) {
        this.id = id;
        this.carModel = carModel;
        this.kitName = kitName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "FaceliftKit{" +
                "id=" + id +
                ", carModel='" + carModel + '\'' +
                ", kitName='" + kitName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
