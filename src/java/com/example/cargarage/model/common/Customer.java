package com.example.cargarage.model.common;



import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity

public class Customer {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;  // Unique identifier for each customer

	    private String name;  // Name of the customer
	    private String email;  // Email of the customer
	    private String mobile;  // Mobile number of the customer
	    private String address;  // Address of the customer
	    //    private String cars;
	    private String password; // Password
	    
	    
	    // DB Connections 
	    
	    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
	    private List<Car> cars;
	    
	    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
	    private List<Appointment> appointments;
	    
	    @ManyToMany(mappedBy = "customer")
	    private List<Discount> discounts;
	    
	    
	    // Constructors
	    public Customer() {}

	    public Customer(Long id, String name, String email, String mobile, String address,/* String cars,*/ String password) {
	        this.id = id;
	        this.name = name;
	        this.email = email;
	        this.mobile = mobile;
	        this.address = address;
	      //  this.cars = cars;
	        this.password = password;
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
	    
	  /*  public String getCars() {
	        return cars;
	    }
	    
	    public void setcars(String cars) {
	        this.cars = cars;
	    } */
	    
	    public String getPassword() {
	        return password;
	    }
	    
	    public void setPassword(String password) {
	        this.password = password;
	    }

	

		    
	    // FOR DTO FILES 
	    
	/*    public List<Car> getCars() {
	        return getCars();
	    }

	    public List<Appointment> getAppointments() {
	        return getAppointments();
	    }
	*/

	}