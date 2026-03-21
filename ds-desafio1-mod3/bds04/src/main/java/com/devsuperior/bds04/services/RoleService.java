package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.requests.RoleRequestDTO;
import com.devsuperior.bds04.dto.responses.RoleResponseDTO;
import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.exceptions.database.DatabaseException;
import com.devsuperior.bds04.exceptions.services.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream().map(RoleResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> findAllPages(Pageable pageable) {
        return roleRepository.findAll(pageable).map(RoleResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO findById(Long id) {
        Role entity = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return new RoleResponseDTO(entity);
    }

    @Transactional
    public RoleResponseDTO create(RoleRequestDTO request) {
        Role entity = request.toEntity();

        entity = roleRepository.save(entity);

        return new RoleResponseDTO(entity);
    }

    @Transactional
    public RoleResponseDTO update(Long id, RoleRequestDTO request) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        Role entity = roleRepository.getReferenceById(id);
        BeanUtils.copyProperties(request, entity, "id");
        entity = roleRepository.save(entity);

        return new RoleResponseDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        try {
            roleRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

}
