// ADMIN SIDE DTO

package com.example.cargarage.dto;

public class AdminAppointmentRequestDTO {
    private String appointmentDate;
    private String serviceType;
    private String status;

    // Getters and Setters
    public String getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public String getServiceType() {
        return serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}