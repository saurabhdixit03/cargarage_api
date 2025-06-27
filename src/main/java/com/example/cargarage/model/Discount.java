package com.example.cargarage.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Entity representing a discount that can be applied to certain services,
 * optionally targeted to specific customers or customer types.
 */
@Entity
public class Discount {

    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic details about the discount
    private String name;
    private String description;
    private Double discountPercentage;

    /**
     * Many-to-Many mapping to the Services entity.
     * A discount can apply to multiple services.
     * This is persisted via a join table: discount_services.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "discount_services",
        joinColumns = @JoinColumn(name = "discount_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Services> services;

    // Date until which the discount is valid
    private Date validUntil;

    // Enum to filter eligible customer type (e.g., NEW, REGULAR, etc.)
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    // Optional: this discount can be linked to a specific customer
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    /**
     * Not mapped to DB.
     * Used to temporarily hold service IDs during API operations.
     * Useful when client sends only IDs instead of full service objects.
     */
    @Transient
    private List<Long> serviceIds;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(Double discountPercentage) { this.discountPercentage = discountPercentage; }

    public List<Services> getServices() { return services; }
    public void setServices(List<Services> services) { this.services = services; }

    public Date getValidUntil() { return validUntil; }
    public void setValidUntil(Date validUntil) { this.validUntil = validUntil; }

    public CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Long> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }
}
