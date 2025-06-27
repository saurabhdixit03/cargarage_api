package com.example.cargarage.dto;

import java.time.LocalDate;
import java.util.List;

public class UserFaceliftKitDTO {
    private Long faceliftKitId;
    private Long carId;
    private String carName;
    private String kitName;
    private String description;
    private double price;
    private List<String> images;

    public UserFaceliftKitDTO() {}

    public UserFaceliftKitDTO(Long faceliftKitId, Long carId, String carName, String kitName, String description, double price, List<String> images) {
        this.faceliftKitId = faceliftKitId;
        this.carId = carId;
        this.carName = carName;
        this.kitName = kitName;
        this.description = description;
        this.price = price;
        this.images = images;
    }

    public Long getFaceliftKitId() {
        return faceliftKitId;
    }

    public void setFaceliftKitId(Long faceliftKitId) {
        this.faceliftKitId = faceliftKitId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
} 
