package com.denkitronik.clienteservice.domain.services;

import com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException;
import com.denkitronik.clienteservice.domain.exception.ClienteServiceException;
import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import com.denkitronik.clienteservice.domain.repositories.IClienteDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteDao.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        try {
            return clienteDao.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            log.error("Causa real del fallo al guardar: {}",
                    ex.getMostSpecificCause().getMessage());
            throw new ClienteServiceException(
                "Error al guardar el cliente: datos duplicados o restricción violada",
                ex
            );
        }
    }

    @Override
    @Transactional
    public Cliente update(Long id, Cliente cliente) {
        Cliente actual = clienteDao.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        actual.setNombre(cliente.getNombre());
        actual.setApellido(cliente.getApellido());
        actual.setEmail(cliente.getEmail());
        actual.setFoto(cliente.getFoto());
        actual.setRegion(cliente.getRegion());
        return clienteDao.save(actual);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!clienteDao.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Region> findAllRegiones() {
        return clienteDao.findAllRegiones();
    }
}