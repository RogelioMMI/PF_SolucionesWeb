package com.example.demo.model.service;

import java.util.List;

import com.example.demo.model.entity.Cliente;

public interface IClienteService {
    public String guardarCliente(Cliente cliente);
    public List<Cliente> cargarClientes();
    public Cliente buscarCliente(Long id);
    public String eliminarCliente(Long id);
    public void registrarCliente (Cliente cliente);
    public boolean existePorEmail(String email);
    public Cliente buscarPorEmail(String email);
}
