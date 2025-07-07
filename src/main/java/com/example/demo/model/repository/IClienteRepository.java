package com.example.demo.model.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.entity.Cliente;

public interface IClienteRepository extends CrudRepository<Cliente, Long>{
    boolean existsByEmail(String email);
    Cliente findByEmail(String email);
}
