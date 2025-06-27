// USER SIDE DTO

package com.example.cargarage.dto;

import java.util.List;

public class UserAppointmentRequestDTO {
    private Long carId;
    private List<Long> serviceIds;
    private String appointmentDate;
    private Long appointmentId;
	private Long discountId;
    private Double budget;
    
    // Getters & Setters 
    public Double getBudget() {	return budget;	}
	public void setBudget(Double budget) {	this.budget = budget;	}

	public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public List<Long> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    
    public Long getDiscountId() { return discountId; }
    public void setDiscountId(Long discountId) { this.discountId = discountId; }
    
}
