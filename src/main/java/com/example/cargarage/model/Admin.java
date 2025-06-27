package com.example.cargarage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// This class represents the 'admin' table in the database.
// It is a JPA entity with fields like id, name, email, and password.
@Entity
@Table(name = "admin")
public class Admin {

    // Primary key with auto-generated value using the database's identity strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Admin's name, email, and password fields
    private String name;
    private String email;
    private String password;

    // Default constructor required by JPA for creating entity instances via reflection
    public Admin() {}

    // Parameterized constructor for creating Admin objects manually
    public Admin(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }    

    // Getters and setters for all fields (used for accessing and updating values)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
