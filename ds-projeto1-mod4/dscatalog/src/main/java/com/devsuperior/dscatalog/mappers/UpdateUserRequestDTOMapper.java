package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.requests.UpdateUserRequestDTO;
import com.devsuperior.dscatalog.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { RoleRequestDTOMapper.class })
public interface UpdateUserRequestDTOMapper extends BeanMapper<UserEntity, UpdateUserRequestDTO> {

}
