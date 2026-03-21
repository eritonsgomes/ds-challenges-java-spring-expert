package com.devsuperior.bds04.services;


import com.devsuperior.bds04.dto.requests.CityRequestDTO;
import com.devsuperior.bds04.dto.responses.CityResponseDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.exceptions.database.DatabaseException;
import com.devsuperior.bds04.exceptions.services.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<CityResponseDTO> findAll() {
        return cityRepository.findAllByNameAsc().stream().map(CityResponseDTO::new).toList();
    }

    public CityResponseDTO save(CityRequestDTO request) {
        City entity = request.toEntity();

        entity = cityRepository.save(entity);

        return new CityResponseDTO(entity);
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
