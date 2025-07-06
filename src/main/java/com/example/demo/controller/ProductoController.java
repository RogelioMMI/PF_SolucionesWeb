/*package com.example.demo.controller;

import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/panel")
    public String panelProductos(HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("producto", new Producto());
        model.addAttribute("productos", productoService.cargarProductos());
        return "admin/productos/panel";
    }

    @PostMapping("/formulario")
    public String guardarProducto(Producto producto, RedirectAttributes flash) {
        String respuesta = productoService.guardarProducto(producto);
        flash.addFlashAttribute("respuestaProducto", respuesta);
        return "redirect:/admin/productos/panel";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("producto", productoService.buscarProducto(id));
        return "admin/productos/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes flash) {
        String respuesta = productoService.eliminarProducto(id);
        flash.addFlashAttribute("respuestaProducto", respuesta);
        return "redirect:/admin/productos/panel";
    }
}
*/

package com.example.demo.controller;

import com.example.demo.model.entity.Producto;
import com.example.demo.model.service.IProductoService;


import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/panel")
    public String panelProductos(HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("producto", new Producto());
        model.addAttribute("productos", productoService.cargarProductos());
        return "admin/productos/panel";
    }

    @PostMapping("/formulario")
public String guardarProducto(@RequestParam(value = "id", required = false) Long id,
                              @RequestParam("nombre") String nombre,
                              @RequestParam("stock") Long stock,
                              @RequestParam("precio") Double precio,
                              @RequestParam("imagen") MultipartFile imagenFile,
                              RedirectAttributes flash) {
    try {
        byte[] imagenBytes = null;

        if (!imagenFile.isEmpty()) {
            String tipo = imagenFile.getContentType();
            long tamaño = imagenFile.getSize();

            if (!"image/jpeg".equals(tipo)) {
                flash.addFlashAttribute("error", "Solo se permiten imágenes JPG.");
                return "redirect:/admin/productos/panel";
            }

            if (tamaño > (5 * 1024 * 1024)) {
                flash.addFlashAttribute("error", "La imagen no puede superar los 5MB.");
                return "redirect:/admin/productos/panel";
            }

            imagenBytes = imagenFile.getBytes();
        }

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setImagen(imagenBytes);

        String respuesta = productoService.guardarProducto(producto);
        flash.addFlashAttribute("respuestaProducto", respuesta);

    } catch (IOException e) {
        flash.addFlashAttribute("error", "Error al cargar la imagen.");
        e.printStackTrace();
    }

    return "redirect:/admin/productos/panel";
}

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, HttpSession session, Model model) {
        if (!"admin".equals(session.getAttribute("rol"))) {
            return "redirect:/admin/login";
        }
        model.addAttribute("producto", productoService.buscarProducto(id));
        
        return "admin/productos/editar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes flash) {
        String respuesta = productoService.eliminarProducto(id);
        flash.addFlashAttribute("respuestaProducto", respuesta);
        return "redirect:/admin/productos/panel";
    }

@GetMapping("/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable Long id){
    Producto producto = productoService.buscarProducto(id);
    if(producto != null && producto.getImagen() != null){
        System.out.println("Se encontró la imagen para el producto: " + id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(producto.getImagen(), headers, HttpStatus.OK);
    } else {
        System.out.println("NO se encontró imagen para el producto: " + id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

}
