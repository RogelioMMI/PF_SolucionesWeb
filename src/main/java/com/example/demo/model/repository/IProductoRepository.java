package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.entity.Producto;

public interface IProductoRepository extends CrudRepository<Producto, Long>{
    // IProductoRepository.java
List<Producto> findByCategoriaIgnoreCase(String categoria);
    
}
