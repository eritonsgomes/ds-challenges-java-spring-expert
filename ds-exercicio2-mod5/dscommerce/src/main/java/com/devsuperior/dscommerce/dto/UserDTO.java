package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private String name;
    private String email;
    private final Set<String> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();

        for (GrantedAuthority role: user.getAuthorities()) {
            roles.add(role.getAuthority());
        }
    }

    public void addRole(Role role) {
        roles.add(role.getAuthority());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
