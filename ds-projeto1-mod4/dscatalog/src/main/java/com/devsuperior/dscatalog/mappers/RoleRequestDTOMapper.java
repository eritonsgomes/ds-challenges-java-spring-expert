package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.requests.RoleRequestDTO;
import com.devsuperior.dscatalog.entities.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleRequestDTOMapper extends BeanMapper<RoleEntity, RoleRequestDTO> {

}
