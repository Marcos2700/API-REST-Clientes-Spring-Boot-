package com.clientes.api.controllers;

import com.clientes.api.entities.Cliente;
import com.clientes.api.repositories.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ClienteController {

    private final Logger log = LoggerFactory.getLogger(Cliente.class);
    private ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @return List of Cliente
     */
    @GetMapping("/api/clientes")
    public List<Cliente> findAll(){
        return repository.findAll();
    }

    /**
     *
     * @param id
     * @return Cliente
     */
    @GetMapping("/api/clientes/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id){
        Optional<Cliente> clienteOpt = repository.findById(id);
        if(clienteOpt.isPresent()){
            log.info("Object Cliente founded successfully");
            return ResponseEntity.ok(clienteOpt.get());
        }
        else{
            log.warn("Object Cliente not founded");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     *
     * @param cliente
     * @return Cliente
     */
    @PostMapping("/api/clientes")
    public ResponseEntity<Cliente> addCliente(@RequestBody Cliente cliente){
        if(cliente.getId() != null){
            log.warn("Bad Request, you are trying to update instead create new Cliente");
            return ResponseEntity.badRequest().build();
        }
        log.info("New Cliente created successfully");
        Cliente resultado = repository.save(cliente);
        return ResponseEntity.ok(resultado);
    }

    /**
     *
     * @param cliente
     * @param id
     * @return Cliente
     */
    @PutMapping("/api/clientes/{id}")
    public ResponseEntity<Cliente> update(@RequestBody Cliente cliente, @PathVariable Optional<Long> id){
        if(!id.isPresent()){
            log.warn("Id is not defined correctly. Id is null so you are trying to create a new Cliente");
            return ResponseEntity.badRequest().build();
        }
        if(!repository.existsById(id.get())){
            log.warn("You are trying to update a Cliente that not exist.");
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente updated successfully.");
        cliente.setId(id.get());
        return ResponseEntity.ok(cliente);
    }

    /**
     *
     * @param id
     * @return ResponseEntity code 204
     */
    @DeleteMapping("/api/clientes/{id}")
    public ResponseEntity<Cliente> delete(@PathVariable Long id){
        Optional<Cliente> clienteOpt = repository.findById(id);
        if(!clienteOpt.isPresent()){
            log.warn("You are trying to delete a Cliente that not exist.");
            return ResponseEntity.notFound().build();
        }
        log.info("Cliente desactivated successfully.");
        clienteOpt.get().setStatus(false);
        repository.save(clienteOpt.get());
        return ResponseEntity.noContent().build();
    }

}
