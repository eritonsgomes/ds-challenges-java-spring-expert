package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.responses.RoleResponseDTO;
import com.devsuperior.dscatalog.entities.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleResponseDTOMapper extends BeanMapper<RoleEntity, RoleResponseDTO> {

}
