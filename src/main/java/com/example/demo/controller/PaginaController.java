package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaginaController {
    @Autowired
    IProductoService productoService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        List<Producto> productos = productoService.cargarProductos();
        model.addAttribute("favoritos", obtenerFavoritosIds(session));
        model.addAttribute("productos", productos);

        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        int cantidad = (carrito != null) ? carrito.size() : 0;
        model.addAttribute("cantidadCarrito", cantidad);
        return "index";
    }

    @GetMapping("/acerca")
    public String mostrarAcerca(){
        return "acerca";
    }

    @GetMapping("/joyeria")
    public String mostrarJoyerias(HttpSession session, Model model) {
        model.addAttribute("productos", productoService.findByCategoriaIgnoreCase("joyeria"));
        model.addAttribute("favoritos", obtenerFavoritosIds(session));
        return "joyeria";
    }

    @GetMapping("/juegos")
    public String mostrarJuegos(HttpSession session, Model model) {
        model.addAttribute("productos", productoService.findByCategoriaIgnoreCase("juegos"));
        model.addAttribute("favoritos", obtenerFavoritosIds(session));
        return "juegos";
    }

    @GetMapping("/prendas")
    public String mostrarPrendas(HttpSession session, Model model) {
        model.addAttribute("productos", productoService.findByCategoriaIgnoreCase("prendas"));
        model.addAttribute("favoritos", obtenerFavoritosIds(session));
        return "prendas";
    }

    private List<Long> obtenerFavoritosIds(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> favoritos = (List<Producto>) session.getAttribute("favoritos");
        List<Long> favoritosIds = new ArrayList<>();
        if (favoritos != null) {
            for (Producto p : favoritos) {
                favoritosIds.add(p.getId());
            }
        }
        return favoritosIds;
    }
}

