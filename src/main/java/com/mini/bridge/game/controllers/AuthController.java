package com.mini.bridge.game.controllers;

import com.mini.bridge.game.models.*;
import com.mini.bridge.game.services.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String start(@ModelAttribute(value = "user") User user, RedirectAttributes model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            authService.save(user);
            model.addFlashAttribute("successMessage", "User " + user.getUsername() + " created");
        } catch (Exception e) {
            model.addFlashAttribute("errorMessage", "Error while creating user " + user.getUsername());
        }

        return "redirect:/login";
    }
}
