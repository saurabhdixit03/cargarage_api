package com.example.cargarage.dto;

import java.time.LocalDate;

public class FaceliftKitBookingRequest {

    private Long faceliftKitId;
    private Long carId;
    private LocalDate dropOffDate;
    private LocalDate pickUpDate; // optional, can be null or empty

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

    public LocalDate getDropOffDate() {
        return dropOffDate;
    }

    public void setDropOffDate(LocalDate dropOffDate) {
        this.dropOffDate = dropOffDate;
    }

    public LocalDate getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDate pickUpDate) {
        this.pickUpDate = pickUpDate;
    }
}
