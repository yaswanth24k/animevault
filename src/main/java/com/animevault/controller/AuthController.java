package com.animevault.controller;

import com.animevault.dto.RegisterForm;
import com.animevault.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {
        if (error != null) model.addAttribute("errorMessage", "Invalid username or password.");
        if (logout != null) model.addAttribute("successMessage", "You have been logged out.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerForm") RegisterForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (!form.passwordsMatch()) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            return "auth/register";
        }

        try {
            userService.register(form);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Account created! Please log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
