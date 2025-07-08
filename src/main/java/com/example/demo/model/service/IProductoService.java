package com.example.demo.model.service;

import java.util.List;

import com.example.demo.model.entity.Producto;

public interface IProductoService {
    public String guardarProducto(Producto producto);
    public List<Producto> cargarProductos();
    public Producto buscarProducto(Long id);
    public String eliminarProducto(Long id);
    public List<Producto> findByCategoriaIgnoreCase(String categoria);
}
