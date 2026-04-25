package com.devsuperior.dscommerce.mappers;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BeanMapper<Product, ProductDTO> {
    
}
