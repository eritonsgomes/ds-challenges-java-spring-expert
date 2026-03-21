package com.devsuperior.bds04.dto.responses;

import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<RoleResponseDTO> roles;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String firstName, String lastName, String email, Set<RoleResponseDTO> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public UserResponseDTO(User entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.roles = Objects.nonNull(entity.getRoles()) ? new HashSet<>() : null;

        for (Role role : entity.getRoles()) {
            this.roles.add(new RoleResponseDTO(role.getId(), role.getAuthority()));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleResponseDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleResponseDTO> roles) {
        this.roles = roles;
    }

}
