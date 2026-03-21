package com.devsuperior.bds04.dto.requests;

import com.devsuperior.bds04.entities.City;
import jakarta.validation.constraints.NotBlank;

public class CityRequestDTO {

	private Long id;

	@NotBlank(message = "Campo requerido")
	private String name;
	
	public CityRequestDTO() {
	}

	public CityRequestDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CityRequestDTO(City entity) {
		id = entity.getId();
		name = entity.getName();
	}

	public City toEntity() {
		return new City(id, name);
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
