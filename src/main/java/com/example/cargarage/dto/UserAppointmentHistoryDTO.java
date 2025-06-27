package com.example.cargarage.dto;

import java.util.List;

public class UserAppointmentHistoryDTO {
    private Long appointmentId;
    private String appointmentDate;
    private String status;
    private String carName; 				// Only the car name (Make + Model)
    private List<String> servicesOpted; 	// List of service names
    private Double budget;
    
    // Constructor
    public UserAppointmentHistoryDTO(Long appointmentId, String appointmentDate, String status, String carName, List<String> servicesOpted, Double budget) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.carName = carName;
        this.servicesOpted = servicesOpted;
        this.budget = budget;
    }

    // Getters & Setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    
    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }

    public List<String> getServicesOpted() { return servicesOpted; }
    public void setServicesOpted(List<String> servicesOpted) { this.servicesOpted = servicesOpted; }

	public Double getBudget() {	return budget;	}
	public void setBudget(Double budget) {	this.budget = budget;	}
        
}
