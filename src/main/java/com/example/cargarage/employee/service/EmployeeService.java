package com.example.cargarage.employee.service;

import com.example.cargarage.model.Employee;
import com.example.cargarage.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;



@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Employee> login(String email, String rawPassword) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (passwordEncoder.matches(rawPassword, employee.getPassword())) {
                return Optional.of(employee);
            }
        }
        return Optional.empty();
    }
    
    public void registerEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    // You can add more methods here (update profile, get assigned tasks, etc.)
    
    // to edit employee details 
    
    public String updateEmployeeDetails(Long employeeId, Map<String, Object> updates) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        if (updates.containsKey("email")) {
            employee.setEmail((String) updates.get("email"));
        }
        
        if (updates.containsKey("name")) {
            employee.setName((String) updates.get("name"));
        }

        if (updates.containsKey("phone")) {
            employee.setPhone((String) updates.get("phone"));
        }

        if (updates.containsKey("role")) {
            employee.setRole((String) updates.get("role"));
        }

        if (updates.containsKey("active")) {
            employee.setActive((Boolean) updates.get("active"));
        }
        
        if (updates.containsKey("password")) {
            String rawPassword = (String) updates.get("password");
            String hashedPassword = passwordEncoder.encode(rawPassword);
            employee.setPassword(hashedPassword);
        }
        
        employeeRepository.save(employee);
        return "Employee updated successfully";
    }

}
