package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.constants.RoleConstants;
import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAllByNameAndCategoryIds(
        @RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") List<String> categoryIds,
        Pageable pageable
    ) {
        Page<ProductResponseDTO> allProducts = productService.findAllByNameAndCategoryIds(name, categoryIds, pageable);
        return ResponseEntity.ok().body(allProducts);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> allProducts = productService.findAll();
        return ResponseEntity.ok().body(allProducts);
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<ProductResponseDTO>> findAllPages(Pageable pageable) {
        Page<ProductResponseDTO> allProducts = productService.findAllPages(pageable);
        return ResponseEntity.ok().body(allProducts);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponseDTO>> findAllByFilter(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") List<String> categoryIds,
            Pageable pageable
    ) {
        Page<ProductResponseDTO> allProducts = productService.findAllByFilter(name, categoryIds, pageable);
        return ResponseEntity.ok().body(allProducts);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        ProductResponseDTO dto = productService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Secured(value = { RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_OPERATOR })
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO request) {
        ProductResponseDTO dto = productService.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @Secured(value = { RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_OPERATOR })
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @RequestBody ProductRequestDTO request) {
        ProductResponseDTO dto = productService.update(id, request);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Secured(value = { RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_OPERATOR })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
