package com.example.cargarage.model;

import java.util.List;

import com.example.cargarage.model.CustomerType;

import jakarta.persistence.*;


@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for each customer

    private String name;  // Name of the customer
    private String email;  // Email of the customer
    private String mobile;  // Mobile number of the customer
    private String address;  // Address of the customer
    private String password; // Password

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type") // 👈 This ensures the column is mapped properly
    private CustomerType customerType;

    // DB Connections  
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @ManyToMany(mappedBy = "customer")
    private List<Discount> discounts;

    // Constructors  
    public Customer() {}

    public Customer(Long id, String name, String email, String mobile, String address, String password, CustomerType customerType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.password = password;
        this.customerType = customerType;
    }

    // Getters and setters  
    public Long getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
}
