package com.example.demo.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.entity.Pedido;

public interface IPedidoRepository extends CrudRepository<Pedido, Long>{
    List<Pedido> findByCliente(Cliente cliente);
}
