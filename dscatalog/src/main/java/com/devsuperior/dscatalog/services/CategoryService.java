package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.mappers.CategoryResponseDTOMapper;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryResponseDTOMapper categoryResponseMapper;

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream().map(categoryResponseMapper::toDTO).toList();
    }

}
