package com.example.demo.controller;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.entity.Pedido;
import com.example.demo.model.service.IClienteService;
import com.example.demo.model.service.IPedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PerfilController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IPedidoService pedidoService;

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        Object idObj = session.getAttribute("clienteId");
        Long clienteId;
        if (idObj instanceof Integer) {
            clienteId = ((Integer) idObj).longValue();
        } else if (idObj instanceof Long) {
            clienteId = (Long) idObj;
        } else {
            return "redirect:/?error=login-requerido";
        }
        Cliente cliente = clienteService.buscarCliente(clienteId);
        List<Pedido> pedidos = pedidoService.buscarPorCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("pedidos", pedidos);

        return "perfil";
    }

    @PostMapping("/perfil/cambiar-contrasena")
    public String cambiarContrasena(
            @RequestParam("actual") String actual,
            @RequestParam("nueva") String nueva,
            HttpSession session,
            Model model) {

        Object idObj = session.getAttribute("clienteId");
        Long clienteId;
        if (idObj instanceof Integer) {
            clienteId = ((Integer) idObj).longValue();
        } else if (idObj instanceof Long) {
            clienteId = (Long) idObj;
        } else {
            model.addAttribute("mensajeCambioContrasena", "Debes iniciar sesión.");
            return "perfil";
        }
        Cliente cliente = clienteService.buscarCliente(clienteId);

        if (!cliente.getClave().equals(actual)) {
            model.addAttribute("mensajeCambioContrasena", "La contraseña actual es incorrecta.");
            model.addAttribute("cliente", cliente);
            model.addAttribute("pedidos", pedidoService.buscarPorCliente(cliente));
            return "perfil";
        }

        if (actual.equals(nueva)) {
            model.addAttribute("mensajeCambioContrasena", "La nueva contraseña debe ser diferente a la actual.");
            model.addAttribute("cliente", cliente);
            model.addAttribute("pedidos", pedidoService.buscarPorCliente(cliente));
            return "perfil";
        }

        cliente.setClave(nueva);
        clienteService.guardarCliente(cliente);

        model.addAttribute("mensajeCambioContrasena", "¡Contraseña actualizada correctamente!");
        model.addAttribute("cliente", cliente);
        model.addAttribute("pedidos", pedidoService.buscarPorCliente(cliente));
        return "perfil";
    }

}