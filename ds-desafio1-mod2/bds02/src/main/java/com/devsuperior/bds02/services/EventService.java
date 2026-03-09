package com.devsuperior.bds02.services;


import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.exceptions.ResourceNotFoundException;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final CityRepository cityRepository;

    @Transactional
    public EventDTO update(Long id, EventDTO request) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        Event event = eventRepository.getReferenceById(id);

        event.setName(request.getName());
        event.setDate(request.getDate());
        event.setUrl(request.getUrl());
        event.setCity(cityRepository.getReferenceById(request.getCityId()));

        event = eventRepository.save(event);

        return new EventDTO(event);
    }

}
