package com.devsuperior.bds04.dto.requests;

import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class UserRequestDTO {

    private Long id;

    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 1, max = 100, message = "{validation.constraints.size}")
    private String firstName;

    @NotBlank(message = "{validation.constraints.not-blank}")
    @Size(min = 1, max = 50, message = "{validation.constraints.size}")
    private String lastName;

    @NotBlank(message = "{validation.constraints.not-blank}")
    @Email(message = "{validation.constraints.email}")
    private String email;

    @Size(min = 1, max = 100)
    private String password;

    @NotEmpty(message = "{validation.constraints.not-empty}")
    private Set<RoleRequestDTO> roles;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String firstName, String lastName, String email, String password, Set<RoleRequestDTO> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserRequestDTO(Long id, String firstName, String lastName, String email, String password, Set<RoleRequestDTO> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserRequestDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = Objects.nonNull(user.getRoles()) ? new HashSet<>() : null;

        for (Role role : user.getRoles()) {
            this.roles.add(new RoleRequestDTO(role.getId(), role.getAuthority()));
        }
    }

    public User toEntity() {
        User user = new User();

        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPassword(this.password);

        for (RoleRequestDTO role : roles) {
            user.addRole(new Role(role.getId(), role.getAuthority()));
        }

        return user;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleRequestDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleRequestDTO> roles) {
        this.roles = roles;
    }

}
