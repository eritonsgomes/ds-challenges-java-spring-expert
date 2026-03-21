package com.devsuperior.bds04.dto.responses;

import com.devsuperior.bds04.entities.Role;

public class RoleResponseDTO {

    private Long id;
    private String authority;

    public RoleResponseDTO() {
    }

    public RoleResponseDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleResponseDTO(Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}
