package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.exceptions.database.DatabaseException;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.mappers.ProductRequestDTOMapper;
import com.devsuperior.dscatalog.mappers.ProductResponseDTOMapper;
import com.devsuperior.dscatalog.repositories.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductResponseDTOMapper productResponseMapper;
    private final ProductRequestDTOMapper productRequestMapper;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream().map(productResponseMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAllPages(Pageable pageable) {
        return productRepository.findAll(pageable).map(productResponseMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return productResponseMapper.toDTO(product);
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO request) {
        ProductEntity entity = productRequestMapper.toEntity(request);
        entity = productRepository.save(entity);
        return productResponseMapper.toDTO(entity);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        ProductEntity entity = productRepository.getReferenceById(id);
        BeanUtils.copyProperties(request, entity);
        entity = productRepository.save(entity);

        return productResponseMapper.toDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
