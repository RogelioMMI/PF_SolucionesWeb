package com.example.demo.model.service;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.entity.Pedido;
import java.util.List;

public interface IPedidoService {
    public String guardarPedido(Pedido pedido);
    public List<Pedido> cargarPedidos();
    public Pedido buscarPedido(Long id);
    public String eliminarPedido(Long id);
    public List<Pedido> buscarPorCliente(Cliente cliente);
}
