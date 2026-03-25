package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.requests.UserRequestDTO;
import com.devsuperior.dscatalog.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { RoleRequestDTOMapper.class })
public interface UserRequestDTOMapper extends BeanMapper<UserEntity, UserRequestDTO> {

}
