package com.devsuperior.bds04.dto.requests;

import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EventRequestDTO {

	private Long id;

	@NotBlank(message = "Campo requerido")
	private String name;

	@FutureOrPresent(message = "A data do evento não pode ser passada")
	private LocalDate date;

	private String url;

	@NotNull(message = "Campo requerido")
	private Long cityId;
	
	public EventRequestDTO() {
	}

	public EventRequestDTO(Long id, String name, LocalDate date, String url, Long cityId) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.url = url;
		this.cityId = cityId;
	}
	
	public EventRequestDTO(Event entity) {
		id = entity.getId();
		name = entity.getName();
		date = entity.getDate();
		url = entity.getUrl();
		cityId = entity.getCity().getId();
	}

	public Event toEntity() {
		Event entity = new Event();

		entity.setId(id);
		entity.setName(name);
		entity.setDate(date);
		entity.setUrl(url);
		entity.setCity(new City(cityId, null));

		return entity;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
}
