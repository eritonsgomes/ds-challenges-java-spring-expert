package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CategoryResponseDTOMapper.class })
public interface ProductResponseDTOMapper extends BeanMapper<ProductEntity, ProductResponseDTO> {

}
