package com.example.cargarage.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a facelift kit associated with a car model.
 * Contains kit details like model, name, description, price, and status.
 */
@Entity
@Table(name = "faceliftkits")
public class FaceliftKit {

    // Primary key with auto-generated value
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Car make this kit is for (e.g., Mahindra)
    private String carMake;

    // Car model this kit is for (e.g., XUV700)
    private String carModel;

    // Name of the facelift kit
    private String kitName;

    // Count of the available kits
    private Long availableCount;

    // Description of the kit
    private String description;

    // Price of the kit
    private Double price;

    // List of image URLs or paths for the kit
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "faceliftkit_images", joinColumns = @JoinColumn(name = "kit_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    // Status flag to enable/disable the kit
    private boolean isActive = true;

    // Default constructor (required by JPA)
    public FaceliftKit() {
    }

    // Parameterized constructor
    public FaceliftKit(Long id, String carMake, String carModel, String kitName, Long availableCount,
                       String description, Double price, List<String> images, boolean isActive) {
        this.id = id;
        this.carMake = carMake;
        this.carModel = carModel;
        this.kitName = kitName;
        this.availableCount = availableCount;
        this.description = description;
        this.price = price;
        this.images = images;
        this.isActive = isActive;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
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

    public Long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Long availableCount) {
        this.availableCount = availableCount;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Useful for debugging and logging
    @Override
    public String toString() {
        return "FaceliftKit{" +
                "id=" + id +
                ", carMake='" + carMake + '\'' +
                ", carModel='" + carModel + '\'' +
                ", kitName='" + kitName + '\'' +
                ", availableCount=" + availableCount +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                ", isActive=" + isActive +
                '}';
    }
}
