package com.example.demo.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Producto;
import com.example.demo.model.repository.IProductoRepository;

@Service
public class ProductoService implements IProductoService{
    @Autowired
    private IProductoRepository productoRepository;


    @Override
    public String guardarProducto(Producto producto) {
        String rpta = "";
        if(producto.getId() == null){
            rpta = "Se registro el producto correctamente";

        }else{
            rpta = "Se actualizo el producto";
        }
        productoRepository.save(producto);
        return rpta;
    }

    @Override
    public List<Producto> cargarProductos() {
        return (List<Producto>)productoRepository.findAll();
    }

    @Override
    public Producto buscarProducto(Long id) {
        return productoRepository.findById(id).get();
    }

    @Override
    public String eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            String nombreImagen = producto.getImagenNombre();
            if (nombreImagen != null && !nombreImagen.isEmpty()) {
                Path ruta = Paths.get("src/main/resources/static/assets").resolve(nombreImagen);
                try {
                    Files.deleteIfExists(ruta);
                } catch (IOException e) {
                    System.err.println("Error eliminando la imagen: " + e.getMessage());
                }
            }
            productoRepository.deleteById(id);
            return "Se eliminó el producto correctamente";
        }
        return "Producto no encontrado";
    }

    @Override
    public List<Producto> findByCategoriaIgnoreCase(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria);
    }
}
