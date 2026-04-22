package com.example.progettoschedly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "forward:/home.html";
    }

    @GetMapping("/")
    public String homee() {
        return "forward:/home.html";
    }
}
