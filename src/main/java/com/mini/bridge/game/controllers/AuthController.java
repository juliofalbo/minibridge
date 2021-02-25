package com.mini.bridge.game.controllers;

import com.mini.bridge.game.models.*;
import com.mini.bridge.game.services.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/public/user")
    public String user(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/public/user")
    public String start(@ModelAttribute(value = "user") User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authService.save(user);
        return "redirect:/login";
    }
}
