package com.example.cargarage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/booking-status"); // ✅ for client subscriptions
        config.setApplicationDestinationPrefixes("/app"); // ✅ for sending messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-status")
                .setAllowedOriginPatterns("http://localhost:3000") // ✅ frontend dev server
                .withSockJS();
    }

}
