package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CategoryRequestDTOMapper.class })
public interface ProductRequestDTOMapper extends BeanMapper<ProductEntity, ProductRequestDTO> {

}
