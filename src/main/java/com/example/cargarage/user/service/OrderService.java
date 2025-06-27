package com.example.cargarage.user.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cargarage.model.Orders;
import com.example.cargarage.repository.OrdersRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Value("${razorpay.api.key}")
    private String razorpayId;

    @Value("${razorpay.api.secret}")
    private String razorpaySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayId, razorpaySecret);
    }

    public Orders createOrder(Orders order) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("amount", order.getAmount() * 100); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", "receipt_" + System.currentTimeMillis());

        Order razorpayOrder = razorpayClient.orders.create(options);

        if (razorpayOrder != null) {
            order.setRazorpayOrderId(razorpayOrder.get("id")); 
            order.setOrderStatus(razorpayOrder.get("status"));
            return ordersRepository.save(order);
        }

        // Return null or custom error object if order fails (optional improvement)
        return order;
    }
    
    
    
}


