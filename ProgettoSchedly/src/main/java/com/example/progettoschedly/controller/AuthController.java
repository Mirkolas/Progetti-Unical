package com.example.progettoschedly.controller;

import com.example.progettoschedly.data.entity.Utente;
import com.example.progettoschedly.data.repository.UtenteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @GetMapping("/login")
    public String login() {
        return "forward:/Autenticazione/Auth.html";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "forward:/Autenticazione/Register.html";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String confirmPassword,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        username = username == null ? "" : username.trim();
        email = email == null ? "" : email.trim();

        if (username.isBlank() || email.isBlank() || password == null || password.isBlank()) {
            return "redirect:/auth/register?error=missing";
        }
        if (confirmPassword != null && !confirmPassword.isBlank() && !password.equals(confirmPassword)) {
            return "redirect:/auth/register?error=nomatch";
        }
        if (utenteRepository.existsByUsername(username)) {
            return "redirect:/auth/register?error=userexists";
        }
        if (utenteRepository.existsByEmail(email)) {
            return "redirect:/auth/register?error=emailexists";
        }

        Utente u = new Utente();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        utenteRepository.save(u);

        // auto-login
        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(username, password)
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        return "redirect:/home";
    }
}
