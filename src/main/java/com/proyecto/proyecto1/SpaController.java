package com.proyecto.proyecto1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({"/", ""})
    public String root() {
        return "forward:/index.html";
    }

    // Catch-all for paths without a dot (so static assets keep working)
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
