package com.example.demo.controller;

import com.example.demo.model.entity.DetallePedido;
import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IDetallePedidoService;
import com.example.demo.model.service.IProductoService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private IDetallePedidoService detalleService;

    @Autowired
    private IProductoService productoService;

    // @GetMapping("/panel")
    // public String panelDetalle(HttpSession session, Model model) {
    //     if (!"admin".equals(session.getAttribute("rol"))) {
    //         return "redirect:/admin/login";
    //     }
    //     model.addAttribute("detalle", new DetallePedido());
    //     model.addAttribute("detalles", detalleService.cargarDetallePedidos());
    //     return "admin/detalle-pedidos/panel";
    // }

    // @PostMapping("/formulario")
    // public String guardarDetalle(DetallePedido detalle, RedirectAttributes flash) {
    //     String respuesta = detalleService.guardarDetallePedido(detalle);
    //     flash.addFlashAttribute("respuestaDetalle", respuesta);
    //     return "redirect:/admin/pedidos/detalle/" + detalle.getPedido().getId();
    // }
    @PostMapping("/formulario")
    public String guardarDetalle(DetallePedido detalle, RedirectAttributes flash) {
        // 🛑 Verifica que tenga pedido y producto
        if (detalle.getPedido() == null || detalle.getProducto() == null) {
            flash.addFlashAttribute("respuestaDetalle", "Faltan datos para guardar el detalle.");
            return "redirect:/admin/pedidos/panel";
        }

        // ✅ Buscar producto en BD para obtener su precio
        Producto producto = productoService.buscarProducto(detalle.getProducto().getId());
        if (producto == null) {
            flash.addFlashAttribute("respuestaDetalle", "Producto no encontrado.");
            return "redirect:/admin/pedidos/panel";
        }

        // 🔧 Asignar precioUnitario desde la BD
        detalle.setPrecioUnitario(producto.getPrecio());

        // 💾 Guardar
        String respuesta = detalleService.guardarDetallePedido(detalle);
        flash.addFlashAttribute("respuestaDetalle", respuesta);
        
        return "redirect:/admin/pedidos/detalle/" + detalle.getPedido().getId();
    }


    // @GetMapping("/editar/{id}")
    // public String editarDetalle(@PathVariable Long id, HttpSession session, Model model) {
    //     if (!"admin".equals(session.getAttribute("rol"))) {
    //         return "redirect:/admin/login";
    //     }
    //     model.addAttribute("detalle", detalleService.buscarDetallePedido(id));
    //     return "admin/detalle-pedidos/editar";
    // }

    @GetMapping("/eliminar/{id}")
    public String eliminarDetalle(@PathVariable Long id, RedirectAttributes flash) {
        DetallePedido detalle = detalleService.buscarDetallePedido(id);
        Long pedidoId = detalle.getPedido().getId();
        String respuesta = detalleService.eliminarDetallePedido(id);
        flash.addFlashAttribute("respuestaDetalle", respuesta);
        return "redirect:/admin/pedidos/detalle/" + pedidoId;
    }
}
