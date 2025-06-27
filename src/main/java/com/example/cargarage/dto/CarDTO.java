// ADMIN SIDE DTO
 
package com.example.cargarage.dto;

import java.util.List;

public class CarDTO {
    private String carModel;
    private String carColor;
    private String licensePlate;
    
    private CustomerDTO owner; 	// Fetches the Respected Owner of Car     
    private List<AdminAppointmentRequestDTO> appointments; // List of appointments for the specific car


    // Getters and Setters
    public String getCarModel() {
        return carModel;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }
    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public CustomerDTO getOwner() {
        return owner;
    }
    public void setOwner(CustomerDTO owner) {
        this.owner = owner;
    }
    
    public List<AdminAppointmentRequestDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AdminAppointmentRequestDTO> appointments) {
        this.appointments = appointments;
    }
}
