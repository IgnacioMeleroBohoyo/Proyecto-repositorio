package com.demo.service;

import com.demo.model.User;
import com.demo.repository.UserRepository;
import com.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Retryable(value = {DataAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public Map<String, Object> register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return Map.of("error", "Usuario ya existe");
        }
        User u = new User(username, passwordEncoder.encode(password), "ROLE_USER");
        userRepository.save(u);
        return Map.of("message", "Usuario creado");
    }

    @Recover
    public Map<String, Object> recover(DataAccessException ex, String username, String password) {
        return Map.of("error", "Servicio temporalmente no disponible, inténtalo más tarde");
    }

    public Map<String, Object> login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtils.generateToken(username);
        return Map.of(
                "token", token,
                "tokenType", "Bearer",
                "username", username
        );
    }
}
