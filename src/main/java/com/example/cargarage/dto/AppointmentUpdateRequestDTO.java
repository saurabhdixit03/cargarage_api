// UDER SIDE DTO

package com.example.cargarage.dto;
import java.util.List;

public class AppointmentUpdateRequestDTO {

    private Long carId;
    private String appointmentDate;
    private List<Long> serviceIds;

    // Getters and setters

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }
    
}
