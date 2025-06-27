// USER SIDE DTO

package com.example.cargarage.dto;

public class UserAppointmentResponseDTO {
    private Long appointmentId;
    private String appointmentDate;
    private String status;
    private String customerName;
    private String mobile;
    private Long carId;
    private String carModel;
    private String licensePlate;
    private String serviceType;
    private Double budget;
    private Long groupId;
    private Long discountId;

    
    // Constructor
	public UserAppointmentResponseDTO(Long appointmentId, String appointmentDate, String status, String customerName, String mobile, Long carId, String carModel, String licensePlate, String serviceType, Double budget,  Long groupId, Long discountId) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.customerName = customerName;
        this.mobile = mobile;
        this.carId = carId;
        this.carModel = carModel;
        this.licensePlate= licensePlate;
        this.serviceType = serviceType;
        this.budget = budget;
        this.groupId = groupId;
        this.discountId = discountId; 
	}

    // Getters & Setters

	public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getCarId() {return carId ;}
    public void setCarId(Long carId) { this.carId = carId; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }
    
    public String getlicensePlate() {return licensePlate; }
    public void setlicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getServiceName() { return this.serviceType; }
    public void setServiceName(String serviceType) { this.serviceType = serviceType; }
    
    public Double getBudget() { return budget;	}
	public void setBudget(Double budget) {	this.budget = budget;	}

	public Long getGroupId() { return groupId;	}
	public void setGroupId(Long groupId) {	this.groupId = groupId;	}
	
	public Long getDiscountId() {return this.discountId; }
	public void setDiscountId(Long discountId ) {this.discountId=discountId;}
	
}