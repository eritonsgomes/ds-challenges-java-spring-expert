package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.requests.CreateUserRequestDTO;
import com.devsuperior.dscatalog.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { RoleRequestDTOMapper.class })
public interface CreateUserRequestDTOMapper extends BeanMapper<UserEntity, CreateUserRequestDTO> {

}
