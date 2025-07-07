package com.example.demo.controller;

import com.example.demo.model.entity.Pedido;
import com.example.demo.model.service.IClienteService;
import com.example.demo.model.service.IPedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoController {

    @Autowired
    private IPedidoService pedidoService;

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/panel")
    public String panelPedidos(HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("pedidos", pedidoService.cargarPedidos());
        return "admin/pedidos/panel";
    }

    @PostMapping("/formulario")
    public String guardarPedido(Pedido pedido, RedirectAttributes flash) {
        String respuesta = pedidoService.guardarPedido(pedido);
        flash.addFlashAttribute("respuestaPedido", respuesta);
        return "redirect:/admin/pedidos/panel";
    }

    @GetMapping("/editar/{id}")
    public String editarPedido(@PathVariable Long id, HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("pedido", pedidoService.buscarPedido(id));
        model.addAttribute("clientes", clienteService.cargarClientes());
        return "admin/pedidos/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id, RedirectAttributes flash) {
        String respuesta = pedidoService.eliminarPedido(id);
        flash.addFlashAttribute("respuestaPedido", respuesta);
        return "redirect:/admin/pedidos/panel";
    }
    @GetMapping("/detalle/{id}")
    public String verDetallePedido(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoService.buscarPedido(id);
        if (pedido == null) {
            return "redirect:/admin/pedidos/panel";
        }
        model.addAttribute("pedido", pedido);
        return "admin/pedidos/detalle";
    }
}
