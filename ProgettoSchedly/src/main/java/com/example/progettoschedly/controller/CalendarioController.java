package com.example.progettoschedly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarioController {

    @GetMapping("/calendario")
    public String calendario() {
        return "forward:/calendario.html";
    }
}

