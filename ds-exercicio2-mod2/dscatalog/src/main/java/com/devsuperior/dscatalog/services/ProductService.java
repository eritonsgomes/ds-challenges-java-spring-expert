package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;
import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.exceptions.database.DatabaseException;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.mappers.ProductRequestDTOMapper;
import com.devsuperior.dscatalog.mappers.ProductResponseDTOMapper;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductResponseDTOMapper productResponseMapper;
    private final ProductRequestDTOMapper productRequestMapper;

    private final CategoryRepository categoryRepository;

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

        addCategoriesToProductEntity(request, entity);

        entity = productRepository.save(entity);

        return productResponseMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    private void addCategoriesToProductEntity(ProductRequestDTO request, ProductEntity entity) {
        Set<CategoryRequestDTO> categoryRequestDTOS = request.categories();
        Set<CategoryEntity> categoryEntities = new HashSet<>();

        for (CategoryRequestDTO categoryRequestDTO : categoryRequestDTOS) {
            Optional<CategoryEntity> categoryFound = Optional.of(
                categoryRepository.getReferenceById(categoryRequestDTO.id())
            );

            CategoryEntity category = categoryFound.orElseThrow(() -> new ResourceNotFoundException(
                MessageFormat.format("A Categoria {0} não foi encontrada", categoryRequestDTO.id()))
            );

            categoryEntities.add(category);
        }

        for (CategoryEntity categoryEntity : categoryEntities) {
            entity.getCategories().add(categoryEntity);
        }
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        ProductEntity entity = productRepository.getReferenceById(id);

        BeanUtils.copyProperties(request, entity);

        entity.getCategories().clear();
        addCategoriesToProductEntity(request, entity);

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
