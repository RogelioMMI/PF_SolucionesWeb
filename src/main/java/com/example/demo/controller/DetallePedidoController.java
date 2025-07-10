package com.example.demo.controller;

import com.example.demo.model.entity.DetallePedido;
import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IDetallePedidoService;
import com.example.demo.model.service.IProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private IDetallePedidoService detalleService;

    @Autowired
    private IProductoService productoService;

    @PostMapping("/formulario")
    public String guardarDetalle(DetallePedido detalle, RedirectAttributes flash) {
        if (detalle.getPedido() == null || detalle.getProducto() == null) {
            flash.addFlashAttribute("respuestaDetalle", "Faltan datos para guardar el detalle.");
            return "redirect:/admin/pedidos/panel";
        }
        Producto producto = productoService.buscarProducto(detalle.getProducto().getId());
        if (producto == null) {
            flash.addFlashAttribute("respuestaDetalle", "Producto no encontrado.");
            return "redirect:/admin/pedidos/panel";
        }

        detalle.setPrecioUnitario(producto.getPrecio());

        String respuesta = detalleService.guardarDetallePedido(detalle);
        flash.addFlashAttribute("respuestaDetalle", respuesta);
        
        return "redirect:/admin/pedidos/detalle/" + detalle.getPedido().getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDetalle(@PathVariable Long id, RedirectAttributes flash) {
        DetallePedido detalle = detalleService.buscarDetallePedido(id);
        Long pedidoId = detalle.getPedido().getId();
        String respuesta = detalleService.eliminarDetallePedido(id);
        flash.addFlashAttribute("respuestaDetalle", respuesta);
        return "redirect:/admin/pedidos/detalle/" + pedidoId;
    }
}
