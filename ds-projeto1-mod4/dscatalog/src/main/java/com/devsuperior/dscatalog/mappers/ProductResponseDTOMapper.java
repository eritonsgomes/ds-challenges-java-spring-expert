package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.projections.ProductProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { CategoryResponseDTOMapper.class })
public interface ProductResponseDTOMapper extends BeanMapper<ProductEntity, ProductResponseDTO> {

    static ProductResponseDTO toResponseDTO(ProductProjection projection) {
        if ( projection == null ) {
            return null;
        }

        ProductResponseDTO.ProductResponseDTOBuilder productResponseDTO = ProductResponseDTO.builder();

        productResponseDTO.id(projection.getId() );
        productResponseDTO.name( projection.getName() );

        return productResponseDTO.build();
    }

}
