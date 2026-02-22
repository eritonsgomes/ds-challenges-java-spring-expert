package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.mappers.CategoryRequestDTOMapper;
import com.devsuperior.dscatalog.mappers.CategoryResponseDTOMapper;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryResponseDTOMapper categoryResponseMapper;
    private final CategoryRequestDTOMapper categoryRequestMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream().map(categoryResponseMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));

        return categoryResponseMapper.toDTO(category);
    }

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO request) {
        CategoryEntity entity = categoryRequestMapper.toEntity(request);
        entity = categoryRepository.save(entity);
        return categoryResponseMapper.toDTO(entity);
    }
}
