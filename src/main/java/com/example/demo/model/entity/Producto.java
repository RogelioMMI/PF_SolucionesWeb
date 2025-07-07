package com.example.demo.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Producto")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "imagen_nombre")
    private String imagenNombre; 

    @Column(name = "categoria")
    private String categoria; // <<<< CAMPO PARA CATEGORÍA

    @Transient
    private int cantidad = 1;

    public Producto() {
    }

    public Producto(String nombre, Double precio, String imagenNombre) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagenNombre = imagenNombre;
        this.cantidad = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagenNombre() {
        return imagenNombre;
    }

    public void setImagenNombre(String imagenNombre) {
        this.imagenNombre = imagenNombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // <<< FALTABA ESTO >>>
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
