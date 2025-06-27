package com.example.cargarage.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cargarage.model.Orders;
import com.example.cargarage.user.service.OrderService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

    @Autowired 
    private OrderService orderService; 

    // Removed broken recursive method

    @PostMapping(value = "/createOrder", produces = "application/json")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders orders) throws RazorpayException {
        Orders razorpayOrder = orderService.createOrder(orders);
        if (razorpayOrder.getRazorpayOrderId() != null) {
            return new ResponseEntity<>(razorpayOrder, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
