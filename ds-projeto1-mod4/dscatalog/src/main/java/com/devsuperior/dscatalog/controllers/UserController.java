package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.constants.RoleConstants;
import com.devsuperior.dscatalog.dtos.requests.CreateUserRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.UpdateUserRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.UserResponseDTO;
import com.devsuperior.dscatalog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/pages")
    @PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
    public ResponseEntity<Page<UserResponseDTO>> findAllPages(Pageable pageable) {
        Page<UserResponseDTO> users = userService.findAllPages(pageable);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserRequestDTO request) {
        UserResponseDTO dto = userService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO request) {
        UserResponseDTO dto = userService.update(id, request);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> findMe() {
        UserResponseDTO dto = userService.findMe();
        return ResponseEntity.ok(dto);
    }

}
