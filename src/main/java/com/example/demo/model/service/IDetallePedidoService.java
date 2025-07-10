package com.example.demo.model.service;

import com.example.demo.model.entity.DetallePedido;
import java.util.List;

public interface IDetallePedidoService {
    public String guardarDetallePedido(DetallePedido detalle);
    public List<DetallePedido> cargarDetallePedidos();
    public DetallePedido buscarDetallePedido(Long id);
    public String eliminarDetallePedido(Long id);
    public void actualizarTotalPedido(DetallePedido detalle);
}
