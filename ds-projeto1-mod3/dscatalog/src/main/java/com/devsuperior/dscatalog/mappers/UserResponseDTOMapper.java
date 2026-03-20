package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.dtos.responses.UserResponseDTO;
import com.devsuperior.dscatalog.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { RoleResponseDTOMapper.class })
public interface UserResponseDTOMapper extends BeanMapper<UserEntity, UserResponseDTO> {

}
