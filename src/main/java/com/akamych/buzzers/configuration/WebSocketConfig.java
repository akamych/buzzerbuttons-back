package com.akamych.buzzers.configuration;

import com.akamych.buzzers.controllers.WebSocketController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                        activeProfile.equalsIgnoreCase("prod")
                                ? "https://buzzers.akamych.com"
                                : "http://localhost:5173"
                )
                .withSockJS();
    }


    @Bean
    public SimpUserRegistry simpUserRegistry() {
        return new DefaultSimpUserRegistry();
    }

}