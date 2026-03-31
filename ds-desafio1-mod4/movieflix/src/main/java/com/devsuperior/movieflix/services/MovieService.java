package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Transactional(readOnly = true)
	public MovieDetailsDTO findById(Long id) {
		Movie movie = movieRepository.findById(id)
				.orElseThrow(
					() -> new ResourceNotFoundException("Recurso não encontrado")
				);

		return new MovieDetailsDTO(movie);
	}

	public Page<MovieCardDTO> findAll(int pageNumber, int pageSize, String sortBy) {
		String[] sortProperties = PageUtils.extractSortPropertyNames(sortBy);
		Sort.Direction sortDirection = PageUtils.extractSortDirection(sortBy);

		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortProperties));

		return movieRepository.findAll(pageable).map(MovieCardDTO::new);
	}

	public Page<MovieCardDTO> findAllByGenreId(Long genreId, int pageNumber, int pageSize, String sortBy) {
		String[] sortProperties = PageUtils.extractSortPropertyNames(sortBy);
		Sort.Direction sortDirection = PageUtils.extractSortDirection(sortBy);

		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortProperties));

		return movieRepository.findByGenre_Id(genreId, pageable).map(MovieCardDTO::new);
	}

}
