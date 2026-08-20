package com.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal UserDetails user) {
        return Map.of("username", user.getUsername(), "authorities", user.getAuthorities());
    }
}
