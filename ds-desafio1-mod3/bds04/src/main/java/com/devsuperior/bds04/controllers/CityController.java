package com.devsuperior.bds04.controllers;


import com.devsuperior.bds04.constants.RoleConstants;
import com.devsuperior.bds04.dto.requests.CityRequestDTO;
import com.devsuperior.bds04.dto.responses.CityResponseDTO;
import com.devsuperior.bds04.services.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<CityResponseDTO>> findAll() {
        List<CityResponseDTO> allCities = cityService.findAll();

        return ResponseEntity.ok().body(allCities);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('" + RoleConstants.ROLE_ADMIN + "')")
    public ResponseEntity<CityResponseDTO> create(@Valid @RequestBody CityRequestDTO request) {
        CityResponseDTO dto = cityService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(dto.getId());

        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('" + RoleConstants.ROLE_ADMIN + "')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        cityService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
