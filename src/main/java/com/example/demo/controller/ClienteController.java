package com.example.demo.controller;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.service.IClienteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/panel")
    public String panelClientes(HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("clientes", clienteService.cargarClientes());
        return "admin/clientes/panel";
    }

    @PostMapping("/formulario")
    public String guardarCliente(Cliente cliente, RedirectAttributes flash) {
        String respuesta = clienteService.guardarCliente(cliente);
        flash.addFlashAttribute("respuestaCliente", respuesta);
        return "redirect:/admin/clientes/panel";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("cliente", clienteService.buscarCliente(id));
        return "admin/clientes/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes flash) {
        String respuesta = clienteService.eliminarCliente(id);
        flash.addFlashAttribute("respuestaCliente", respuesta);
        return "redirect:/admin/clientes/panel";
    }
}
