package com.akamych.buzzers.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

@Configuration
@EnableWebSocketMessageBroker
@Profile("prod")
public class WebSocketConfigProd implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(
                        "https://buzzers.akamych.com",
                        "http://10.0.2.2:*",
                        "http://192.168.*.*:*",
                        "capacitor://*",
                        "http://*:8100",
                        "http://localhost:8100",
                        "https://localhost:8100",
                        "https://localhost"
                )
                .withSockJS();
    }


    @Bean
    public SimpUserRegistry simpUserRegistry() {
        return new DefaultSimpUserRegistry();
    }

}