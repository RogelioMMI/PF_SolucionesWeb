package com.example.demo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Cliente;
import com.example.demo.model.repository.IClienteRepository;

@Service
public class ClienteService implements IClienteService{
    @Autowired
    private IClienteRepository clienteRepository;


    @Override
    public String guardarCliente(Cliente cliente) {
        String rpta = "";
        if(cliente.getId() == null){
            rpta = "Se registro el cliente correctamente";

        }else{
            rpta = "Se actualizo el cliente";
        }
        clienteRepository.save(cliente);
        return rpta;
    }

    @Override
    public List<Cliente> cargarClientes() {
        return (List<Cliente>)clienteRepository.findAll();

    }

    @Override
    public Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id).get();
    }

    @Override
    public String eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
        return "Se elimino el cliente";
    }

    @Override
    public void registrarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public boolean existePorEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

}
