package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryResponseDTOMapper extends BeanMapper<CategoryEntity, CategoryResponseDTO> {

}
