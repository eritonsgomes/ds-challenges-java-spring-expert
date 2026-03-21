package com.devsuperior.bds04.controllers;

import com.devsuperior.bds04.constants.RoleConstants;
import com.devsuperior.bds04.dto.requests.RoleRequestDTO;
import com.devsuperior.bds04.dto.responses.RoleResponseDTO;
import com.devsuperior.bds04.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/roles")
@PreAuthorize("hasAnyRole(" + RoleConstants.ROLE_ADMIN + ")")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> findAll() {
        List<RoleResponseDTO> roles = roleService.findAll();
        return ResponseEntity.ok().body(roles);
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<RoleResponseDTO>> findAllPages(Pageable pageable) {
        Page<RoleResponseDTO> roles = roleService.findAllPages(pageable);
        return ResponseEntity.ok().body(roles);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleResponseDTO> findById(@PathVariable Long id) {
        RoleResponseDTO dto = roleService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@RequestBody RoleRequestDTO request) {
        RoleResponseDTO dto = roleService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Long id, @RequestBody RoleRequestDTO request) {
        RoleResponseDTO dto = roleService.update(id, request);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
