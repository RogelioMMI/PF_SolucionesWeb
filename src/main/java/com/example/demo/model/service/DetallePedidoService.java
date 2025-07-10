package com.example.demo.model.service;

import com.example.demo.model.entity.DetallePedido;
import com.example.demo.model.entity.Pedido;
import com.example.demo.model.repository.IDetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallePedidoService implements IDetallePedidoService {

    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    @Autowired
    private IPedidoService pedidoService;

    @Override
    public String guardarDetallePedido(DetallePedido detalle) {
        String rpta = (detalle.getId() == null)
            ? "Se registró el detalle de pedido correctamente"
            : "Se actualizó el detalle de pedido";

        detallePedidoRepository.save(detalle);
        actualizarTotalPedido(detalle);
        return rpta;
    }

    @Override
    public List<DetallePedido> cargarDetallePedidos() {
        return (List<DetallePedido>) detallePedidoRepository.findAll();
    }

    @Override
    public DetallePedido buscarDetallePedido(Long id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }

    @Override
    public String eliminarDetallePedido(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id).orElse(null);
        if (detalle == null) return "Detalle no encontrado";

        Pedido pedido = detalle.getPedido();
        detallePedidoRepository.deleteById(id);

        actualizarTotalPedido(detalle);
        return "Se eliminó el detalle de pedido";
    }

    @Override
    public void actualizarTotalPedido(DetallePedido detalle) {
        Pedido pedido = pedidoService.buscarPedido(detalle.getPedido().getId());

        double total = pedido.getDetalles()
            .stream()
            .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
            .sum();

        pedido.setTotal_pedido(total);
        pedidoService.guardarPedido(pedido);
    }
}
