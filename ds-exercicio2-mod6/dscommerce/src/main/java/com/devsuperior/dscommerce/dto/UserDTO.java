package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private final Set<String> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String phone, String email, LocalDate birthDate) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.birthDate = user.getBirthDate();

        for (GrantedAuthority role: user.getAuthorities()) {
            roles.add(role.getAuthority());
        }
    }

    public void addRole(Role role) {
        roles.add(role.getAuthority());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
