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
        if (idObj == null) {
            return "redirect:/?error=login-requerido";
        }
        Long clienteId = ((Integer) idObj).longValue();
        Cliente cliente = clienteService.buscarCliente(clienteId);
        List<Pedido> pedidos = pedidoService.buscarPorCliente(cliente);

        model.addAttribute("cliente", cliente);
        model.addAttribute("pedidos", pedidos);

        return "perfil";
    }
}