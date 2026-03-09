package com.devsuperior.bds02.entities;

import java.util.ArrayList;
import java.util.List;

import com.devsuperior.bds02.dto.CityDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_city")
public class City {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "city")
	private List<Event> events = new ArrayList<>();
	
	public City() {
	}

	public City(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public City(CityDTO request) {
		id = request.getId();
		name = request.getName();
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

	public List<Event> getEvents() {
		return events;
	}
}
