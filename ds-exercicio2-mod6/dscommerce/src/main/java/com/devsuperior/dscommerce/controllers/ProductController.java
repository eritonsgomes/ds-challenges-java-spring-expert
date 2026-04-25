package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

public interface ProductController {

    @GetMapping(value = "/{id}")
    ResponseEntity<ProductDTO> findById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<Page<ProductMinDTO>> findAll(
            @RequestParam(name = "name", defaultValue = "") String name,
            Pageable pageable);

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto);

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto);

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity<Void> delete(@PathVariable Long id);

}
