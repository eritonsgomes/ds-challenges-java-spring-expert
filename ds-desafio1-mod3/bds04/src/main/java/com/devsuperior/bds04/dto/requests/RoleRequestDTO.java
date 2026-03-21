package com.devsuperior.bds04.dto.requests;


import com.devsuperior.bds04.entities.Role;

public class RoleRequestDTO {

    private Long id;
    private String authority;

    public RoleRequestDTO() {
    }

    public RoleRequestDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleRequestDTO(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }

    public Role toEntity() {
        return new Role(this.id, this.authority);
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
