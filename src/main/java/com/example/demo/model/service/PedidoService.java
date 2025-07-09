package com.example.demo.model.service;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.entity.Pedido;
import com.example.demo.model.repository.IPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Override
    public String guardarPedido(Pedido pedido) {
        String rpta = (pedido.getId() == null)
            ? "Se registró el pedido correctamente"
            : "Se actualizó el pedido";
        pedidoRepository.save(pedido);
        return rpta;
    }

    @Override
    public List<Pedido> cargarPedidos() {
        return (List<Pedido>) pedidoRepository.findAll();
    }

    @Override
    public Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Override
    public String eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
        return "Se eliminó el pedido";
    }

    @Override
    public List<Pedido> buscarPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }
}
