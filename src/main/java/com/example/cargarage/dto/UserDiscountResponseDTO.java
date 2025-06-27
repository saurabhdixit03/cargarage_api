// USER SIDE DTO

package com.example.cargarage.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.example.cargarage.model.CustomerType;

public class UserDiscountResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double discountPercentage;
    private Date validUntil;
    private CustomerType customerType;
    private Long customerId;
    private List<Long> serviceIds;
    private List<String> serviceNames;
    private Long discountId;
    
    
    // Getters and Setters
    
	public Long getId() {
		return id;
	}
	public Long getDiscountId() {
		return discountId;
	}
	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public Date getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}
	public CustomerType getCustomerType() {
		return customerType;
	}
	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public List<Long> getServiceIds() {
		return serviceIds;
	}
	public void setServiceIds(List<Long> long1) {
		this.serviceIds = long1;
	}
	public List<String> getServiceNames() {
		return serviceNames;
	}
	public void setServiceNames(List<String> serviceNames) {
		this.serviceNames = serviceNames;
	}
	

}
