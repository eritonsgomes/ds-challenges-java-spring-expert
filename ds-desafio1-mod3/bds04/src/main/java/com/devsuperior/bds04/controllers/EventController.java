package com.devsuperior.bds04.controllers;

import com.devsuperior.bds04.constants.RoleConstants;
import com.devsuperior.bds04.dto.requests.EventRequestDTO;
import com.devsuperior.bds04.dto.responses.EventResponseDTO;
import com.devsuperior.bds04.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id, @RequestBody EventRequestDTO request) {
        EventResponseDTO dto = eventService.update(id, request);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('" + RoleConstants.ROLE_ADMIN + "', '" + RoleConstants.ROLE_OPERATOR + "')")
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO request) {
        EventResponseDTO dto = eventService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(dto.getId());

        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public Page<EventResponseDTO> findAllPages(Pageable pageable) {
        return eventService.findAll(pageable);
    }

}
