package com.devsuperior.bds04.services;


import com.devsuperior.bds04.dto.requests.EventRequestDTO;
import com.devsuperior.bds04.dto.responses.EventResponseDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.exceptions.services.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public EventResponseDTO save(EventRequestDTO request) {
        Event entity = request.toEntity();

        addCityToEntity(request, entity);

        entity = eventRepository.save(entity);

        return new EventResponseDTO(entity);
    }

    @Transactional(readOnly = true)
    public void addCityToEntity(EventRequestDTO request, Event entity) {
        if (!cityRepository.existsById(request.getCityId())) {
            throw new ResourceNotFoundException(
                    MessageFormat.format("A Cidade {0} não foi encontrada", request.getCityId())
            );
        }

        City city = cityRepository.getReferenceById(request.getCityId());
        entity.setCity(city);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseDTO> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable).map(EventResponseDTO::new);
    }

    @Transactional
    public EventResponseDTO update(Long id, EventRequestDTO request) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        Event event = eventRepository.getReferenceById(id);

        event.setName(request.getName());
        event.setDate(request.getDate());
        event.setUrl(request.getUrl());
        event.setCity(cityRepository.getReferenceById(request.getCityId()));

        event = eventRepository.save(event);

        return new EventResponseDTO(event);
    }

}
