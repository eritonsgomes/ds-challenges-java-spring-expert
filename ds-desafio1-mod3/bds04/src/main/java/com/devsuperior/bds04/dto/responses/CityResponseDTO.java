package com.devsuperior.bds04.dto.responses;

import com.devsuperior.bds04.entities.City;

public class CityResponseDTO {

	private Long id;
	private String name;

	public CityResponseDTO() {
	}

	public CityResponseDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CityResponseDTO(City entity) {
		id = entity.getId();
		name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
