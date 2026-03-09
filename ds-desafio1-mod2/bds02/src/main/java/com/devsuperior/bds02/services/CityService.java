package com.devsuperior.bds02.services;


import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.exceptions.DatabaseException;
import com.devsuperior.bds02.exceptions.ResourceNotFoundException;
import com.devsuperior.bds02.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public List<CityDTO> findAll() {
        return cityRepository.findAllByNameAsc().stream().map(CityDTO::new).toList();
    }

    public CityDTO save(CityDTO request) {
        City entity = new City(request);
        entity = cityRepository.save(entity);
        return new CityDTO(entity);
    }

    @Transactional(readOnly = true)
    public void deleteById(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }

        cityRepository.findById(id).ifPresent(city -> {
            boolean hasEmptyCities = city.getEvents().isEmpty();

            if (!hasEmptyCities) {
                throw new DatabaseException("Falha de integridade referencial");
            }
        });

        try {
            cityRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

}
