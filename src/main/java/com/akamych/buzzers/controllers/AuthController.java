package com.akamych.buzzers.controllers;

import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.repositories.UserRepository;
import com.akamych.buzzers.services.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthController {
    private final JwtService jwtService ;
    private final UserRepository userRepository;

    public AuthController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/host")
    public String generateToken(@RequestParam String role) {
//        User user = new User(null, role, null);
//        String token = jwtService.generateToken(user.getId().toString(), role.name());
//        user.setJwtToken(token);
//        userRepository.save(user);
//        return token;
        return "ghj";
    }
}