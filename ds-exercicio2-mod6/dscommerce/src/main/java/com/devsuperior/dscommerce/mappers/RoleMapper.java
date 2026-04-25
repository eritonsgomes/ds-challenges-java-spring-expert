package com.devsuperior.dscommerce.mappers;

import com.devsuperior.dscommerce.dto.RoleDTO;
import com.devsuperior.dscommerce.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends BeanMapper<Role, RoleDTO> {

}
