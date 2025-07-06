/*package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String agregarAlCarrito(
    @RequestParam String nombre,
    @RequestParam double precio,
    @RequestParam String imagen,
    @RequestParam(required = false, defaultValue = "") String redirect,
    HttpSession session
) {
    List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
    if(carrito == null){
        carrito = new ArrayList<>();
    }
    boolean encontrado = false;
    for (Producto p : carrito) {
        if (p.getNombre().equals(nombre)) {
            p.setCantidad(p.getCantidad() + 1);
            encontrado = true;
            break;
        }
    }
    if (!encontrado) {
        Producto nuevo = new Producto(nombre, precio, imagen);
        nuevo.setCantidad(1);
        carrito.add(nuevo);
    }
    session.setAttribute("carrito", carrito);

    if ("joyeria".equals(redirect)) {
        return "redirect:/joyeria";
    } else if ("prendas".equals(redirect)) {
        return "redirect:/prendas";
    } else if ("juegos".equals(redirect)) {
        return "redirect:/juegos";
    }
    return "redirect:/";
}

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        int cantidad = (carrito != null) ? carrito.size() : 0;
        model.addAttribute("cantidadCarrito", cantidad);
        return "index";
    }

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
}
*/

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

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        int cantidad = (carrito != null) ? carrito.size() : 0;
        model.addAttribute("cantidadCarrito", cantidad);
        model.addAttribute("productos", productoService.cargarProductos());
        
        return "index";
    }

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
        // Obtener cliente
        Integer clienteIdInt = (Integer) session.getAttribute("clienteId");
        if (clienteIdInt == null) {
            return "redirect:/login?error=no-cliente";
        }
        Long clienteId = clienteIdInt.longValue();
        Cliente cliente = clienteService.buscarCliente(clienteId);
    
        // Crear nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado("Pendiente");
        double total = 0.0;
        
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        List<DetallePedido> detalles = new ArrayList<>();

        for (Producto p : carrito) {
            if (p.getId() == null) {
            continue; // o redirige con mensaje de error
            }

        // Cargar producto desde BD (por si acaso)
        Producto productoBD = productoService.buscarProducto(p.getId());

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(productoBD); // asegúrate que está manejado por la base de datos
        detalle.setCantidad(p.getCantidad());
        detalle.setPrecioUnitario(p.getPrecio());
        detalle.setPedido(pedido); // vínculo bidireccional

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

