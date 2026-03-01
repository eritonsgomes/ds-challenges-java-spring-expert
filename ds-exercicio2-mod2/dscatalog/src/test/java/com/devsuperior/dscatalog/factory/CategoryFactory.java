package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;

public class CategoryFactory {

    public static CategoryEntity createCategoryEntity() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(2L);
        categoryEntity.setName("Eletrônicos");

        return categoryEntity;
    }

    public static CategoryResponseDTO createCategoryResponseDto() {
        return CategoryResponseDTO.builder()
                .id(2L)
                .name("Eletrônicos")
                .build();
    }

    public static CategoryRequestDTO createCategoryRequestDto() {
        return CategoryRequestDTO.builder()
                .id(2L)
                .name("Eletrônicos")
                .build();
    }

}
