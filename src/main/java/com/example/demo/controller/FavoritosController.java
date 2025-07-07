package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IProductoService;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
public class FavoritosController {

    @Autowired
    IProductoService productoService;

    @SuppressWarnings("unchecked")
    @PostMapping("/favoritos/agregar")
    public String agregarAFavoritos(@RequestParam Long id,

    @RequestParam(required = false, defaultValue = "") String redirect, HttpSession session) {
    Producto producto = productoService.buscarProducto(id);
    if (producto == null) {
        return "redirect:/";
    }
    List<Producto> favoritos = (List<Producto>) session.getAttribute("favoritos");
    if (favoritos == null) {
        favoritos = new ArrayList<>();
    }

    boolean yaExiste = favoritos.stream()
        .anyMatch(p -> p.getId().equals(producto.getId()));

    if (!yaExiste) {
        favoritos.add(producto);
    }

    session.setAttribute("favoritos", favoritos);

    // Redirige a la página de origen
    if ("index".equals(redirect)) {
        return "redirect:/";
    } else if ("juegos".equals(redirect)) {
        return "redirect:/juegos";
    } else if ("joyeria".equals(redirect)) {
        return "redirect:/joyeria";
    } else if ("prendas".equals(redirect)) {
        return "redirect:/prendas";
    }

    return "redirect:/"; 
}

    @SuppressWarnings("unchecked")
    @GetMapping("/favoritos")
    public String verFavoritos(HttpSession session, Model model) {
        List<Producto> favoritos = (List<Producto>) session.getAttribute("favoritos");
        if (favoritos == null) {
            favoritos = new ArrayList<>();
        }
        model.addAttribute("favoritos", favoritos);
        model.addAttribute("cantidadFavoritos", favoritos.size());

        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        model.addAttribute("cantidadCarrito", (carrito != null) ? carrito.size() : 0);
        return "favoritos"; 
    }

   @PostMapping("/favoritos/eliminar")
    public String eliminarDeFavoritos(@RequestParam int index, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> favoritos = (List<Producto>) session.getAttribute("favoritos");
        if (favoritos != null && index >= 0 && index < favoritos.size()) {
        favoritos.remove(index);
    }
        session.setAttribute("favoritos", favoritos);
        return "redirect:/favoritos";
}

}