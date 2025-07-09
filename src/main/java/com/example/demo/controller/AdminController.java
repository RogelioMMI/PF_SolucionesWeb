package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
        @RequestParam String usuario,
        @RequestParam String contrasena,
        HttpSession session,
        Model model) {
        if ("admin".equals(usuario) && "admin123".equals(contrasena)) {
            session.setAttribute("rol", "admin");
            return "redirect:/admin/inicio";
        }
        model.addAttribute("error", "Credenciales inválidas");
        return "admin/login";
    }
    
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        return "admin/inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
