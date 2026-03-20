package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryRequestDTOMapper extends BeanMapper<CategoryEntity, CategoryRequestDTO> {

}
