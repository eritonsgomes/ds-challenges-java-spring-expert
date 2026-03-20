package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.constants.RoleConstants;
import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<CategoryResponseDTO> allCategories = categoryService.findAll();
        return ResponseEntity.ok().body(allCategories);
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<CategoryResponseDTO>> findAllPages(Pageable pageable) {
        Page<CategoryResponseDTO> allCategories = categoryService.findAllPages(pageable);
        return ResponseEntity.ok().body(allCategories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        CategoryResponseDTO dto = categoryService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Secured(value = { RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_OPERATOR })
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO dto = categoryService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @Secured(value = { RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_OPERATOR })
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO dto = categoryService.update(id, request);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Secured(value = { RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_OPERATOR })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
