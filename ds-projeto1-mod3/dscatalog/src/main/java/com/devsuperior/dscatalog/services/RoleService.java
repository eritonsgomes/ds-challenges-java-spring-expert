package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.RoleRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.RoleResponseDTO;
import com.devsuperior.dscatalog.entities.RoleEntity;
import com.devsuperior.dscatalog.exceptions.database.DatabaseException;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.mappers.RoleRequestDTOMapper;
import com.devsuperior.dscatalog.mappers.RoleResponseDTOMapper;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleResponseDTOMapper roleResponseMapper;
    private final RoleRequestDTOMapper roleRequestMapper;

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findAll() {
        return roleRepository.findAll().stream().map(roleResponseMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> findAllPages(Pageable pageable) {
        return roleRepository.findAll(pageable).map(roleResponseMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO findById(Long id) {
        RoleEntity entity = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return roleResponseMapper.toDTO(entity);
    }

    @Transactional
    public RoleResponseDTO create(RoleRequestDTO request) {
        RoleEntity entity = roleRequestMapper.toEntity(request);
        entity = roleRepository.save(entity);
        return roleResponseMapper.toDTO(entity);
    }

    @Transactional
    public RoleResponseDTO update(Long id, RoleRequestDTO request) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        RoleEntity entity = roleRepository.getReferenceById(id);
        BeanUtils.copyProperties(request, entity, "id");
        entity = roleRepository.save(entity);

        return roleResponseMapper.toDTO(entity);
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
