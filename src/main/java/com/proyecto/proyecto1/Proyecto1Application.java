package com.proyecto.proyecto1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {

    @GetMapping("/")
    public String inicio() {
        return "Hola, Spring Boot funciona correctamente.";
    }

    @GetMapping("/saludo")
    public String saludo() {
        return "Bienvenido a mi primera aplicación Spring Boot.";
    }
}