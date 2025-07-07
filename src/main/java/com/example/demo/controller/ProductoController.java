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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    private final String UPLOAD_DIR = "src/main/resources/static/assets/";

    public static String quitarTildes(String texto) {
        if (texto == null) return null;
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

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
                                   @RequestParam("categoria") String categoria,
                                   @RequestParam("imagen") MultipartFile imagenFile,
                                   RedirectAttributes flash) {
        try {
            String imagenNombre = null;

            if (!imagenFile.isEmpty()) {
                String tipo = imagenFile.getContentType();
                long tamaño = imagenFile.getSize();

                if (!tipo.equals("image/jpeg") && !tipo.equals("image/png")) {
                    flash.addFlashAttribute("error", "Solo se permiten imágenes JPG o PNG.");
                    return "redirect:/admin/productos/panel";
                }

                if (tamaño > (5 * 1024 * 1024)) {
                    flash.addFlashAttribute("error", "La imagen no puede superar los 5MB.");
                    return "redirect:/admin/productos/panel";
                }

                imagenNombre = imagenFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + imagenNombre);
                Files.write(path, imagenFile.getBytes());
            }

            Producto producto = new Producto();
            producto.setId(id);
            producto.setNombre(nombre);
            producto.setStock(stock);
            producto.setPrecio(precio);
            producto.setCategoria(categoria);
            producto.setImagenNombre(imagenNombre);

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

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        List<Producto> productos = productoService.cargarProductos();

        List<Producto> juegos = productos.stream()
                .filter(p -> quitarTildes(p.getCategoria()).equals("juegos"))
                .toList();

        List<Producto> prendas = productos.stream()
                .filter(p -> quitarTildes(p.getCategoria()).equals("prendas"))
                .toList();

        List<Producto> joyeria = productos.stream()
                .filter(p -> quitarTildes(p.getCategoria()).equals("joyeria"))
                .toList();

        model.addAttribute("juegos", juegos);
        model.addAttribute("prendas", prendas);
        model.addAttribute("joyeria", joyeria);

        return "cliente/index";
    }

    @GetMapping("/juegos")
    public String verJuegos(Model model) {
        List<Producto> juegos = productoService.cargarProductos().stream()
                .filter(p -> quitarTildes(p.getCategoria()).equals("juegos"))
                .toList();
        model.addAttribute("productos", juegos);
        return "juegos";
    }

    @GetMapping("/prendas")
    public String verPrendas(Model model) {
        List<Producto> prendas = productoService.cargarProductos().stream()
                .filter(p -> quitarTildes(p.getCategoria()).equals("prendas"))
                .toList();
        model.addAttribute("productos", prendas);
        return "prendas";
    }

    @GetMapping("/joyeria")
    public String verJoyeria(Model model) {
        List<Producto> joyeria = productoService.cargarProductos().stream()
                .filter(p -> p.getCategoria() != null && p.getCategoria().equalsIgnoreCase("joyeria"))
                .toList();
        model.addAttribute("productos", joyeria);
        return "joyeria";
    }

    @GetMapping("/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable Long id) throws IOException {
        Producto producto = productoService.buscarProducto(id);
        if (producto != null && producto.getImagenNombre() != null) {
            Path path = Paths.get("src/main/resources/static/assets/" + producto.getImagenNombre());
            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }
            byte[] imageBytes = Files.readAllBytes(path);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }
}
