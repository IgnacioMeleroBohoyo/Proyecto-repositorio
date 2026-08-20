package com.demo.controller;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.security.JwtUtils;
import com.demo.dto.AuthRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final com.demo.service.AuthService authService;

    public AuthController(com.demo.service.AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest body) {
        String username = body.getUsername();
        String password = body.getPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Usuario y contraseña son requeridos"));
        }
        var res = authService.register(username, password);
        if (res.containsKey("error")) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(res);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest body) {
        String username = body.getUsername();
        String password = body.getPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Credenciales incompletas"));
        }
        try {
            var res = authService.login(username, password);
            return ResponseEntity.ok(res);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales inválidas"));
        }
    }
}
