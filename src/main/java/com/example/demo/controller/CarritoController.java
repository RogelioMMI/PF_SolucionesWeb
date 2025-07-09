package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.entity.DetallePedido;
import com.example.demo.model.entity.Pedido;
import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IClienteService;
import com.example.demo.model.service.IPedidoService;
import com.example.demo.model.service.IProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarritoController {
    @Autowired
    private IProductoService productoService;
    @Autowired
    private IPedidoService pedidoService;
    @Autowired
    private IClienteService clienteService;

    @SuppressWarnings("unchecked")

    @PostMapping("/agregar-al-carrito")
    public String agregarAlCarrito(@RequestParam("id") Long id, @RequestParam("redirect") String redirect, HttpSession session) {
    Producto producto = productoService.buscarProducto(id);
    if (producto == null) {
        return "redirect:/" + redirect + "?error=producto-no-encontrado";
    }

    // Cargar o crear el carrito en sesión
    List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
    if (carrito == null) {
        carrito = new ArrayList<>();
    }

    // Buscar si ya existe en el carrito
    Optional<Producto> existente = carrito.stream()
        .filter(p -> p.getId().equals(id))
        .findFirst();

    if (existente.isPresent()) {
        existente.get().setCantidad(existente.get().getCantidad() + 1);
    } else {
        producto.setCantidad(1);
        carrito.add(producto);
    }

    session.setAttribute("carrito", carrito);

    return switch (redirect) {
        case "index" -> "redirect:/";
        case "juegos" -> "redirect:/juegos";
        case "joyeria" -> "redirect:/joyeria";
        case "prendas" -> "redirect:/prendas";
        case "favoritos" -> "redirect:/favoritos";
        default -> "redirect:/";
    };
    }

    // @GetMapping("/")
    // public String index(HttpSession session, Model model) {
    //     List<Producto> productos = productoService.cargarProductos();
    //     model.addAttribute("productos", productos);

    //     @SuppressWarnings("unchecked")
    //     List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
    //     int cantidad = (carrito != null) ? carrito.size() : 0;
    //     model.addAttribute("cantidadCarrito", cantidad);
    //     return "index";
    // }

    @GetMapping("/carrito")
    public String verCarrito(@RequestParam(required = false, defaultValue = "") String from, HttpSession session, Model model){
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if(carrito == null){
        carrito = new ArrayList<>();
    }
    double total = 0;
    for (Producto p : carrito) {
        total += p.getPrecio() * p.getCantidad();
    }
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", total);
        model.addAttribute("from", from);
        return "carrito";
}

    @PostMapping("/carrito/sumar")
    public String sumarCantidad(@RequestParam int index, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null && index >= 0 && index < carrito.size()) {
            Producto producto = carrito.get(index);
            producto.setCantidad(producto.getCantidad() + 1);
        }
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/restar")
    public String restarCantidad(@RequestParam int index, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null && index >= 0 && index < carrito.size()) {
            Producto producto = carrito.get(index);
            if (producto.getCantidad() > 1) {
                producto.setCantidad(producto.getCantidad() - 1);
            }
        }
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarProducto(@RequestParam int index, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null && index >= 0 && index < carrito.size()) {
            carrito.remove(index);
        }
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    @GetMapping("/carrito/finalizar-compra")
    public String finalizarCompra(HttpSession session){
        Object idObj = session.getAttribute("clienteId");
        if (idObj == null) {
            return "redirect:/login?error=no-cliente";
        }
        Long clienteId = ((Number) idObj).longValue();
        Cliente cliente = clienteService.buscarCliente(clienteId);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado("Pendiente");
        double total = 0.0;
        
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        List<DetallePedido> detalles = new ArrayList<>();

        for (Producto p : carrito) {
            if (p.getId() == null) {
            continue;
            }

            Producto productoBD = productoService.buscarProducto(p.getId());

            Long nuevoStock = productoBD.getStock() - p.getCantidad();
            productoBD.setStock(nuevoStock);
            productoService.guardarProducto(productoBD);

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(productoBD);
            detalle.setCantidad(p.getCantidad());
            detalle.setPrecioUnitario(p.getPrecio());
            detalle.setPedido(pedido);

            detalles.add(detalle);
            total += p.getCantidad() * p.getPrecio();
        }

        pedido.setTotal_pedido(total);
        pedido.setDetalles(detalles);

        pedidoService.guardarPedido(pedido);

        session.removeAttribute("carrito");
        return "redirect:/carrito";
    }
}




