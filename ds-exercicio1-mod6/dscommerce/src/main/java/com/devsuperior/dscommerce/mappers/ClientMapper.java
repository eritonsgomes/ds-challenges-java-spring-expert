package com.devsuperior.dscommerce.mappers;

import com.devsuperior.dscommerce.dto.ClientDTO;
import com.devsuperior.dscommerce.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends BeanMapper<User, ClientDTO> {

}

