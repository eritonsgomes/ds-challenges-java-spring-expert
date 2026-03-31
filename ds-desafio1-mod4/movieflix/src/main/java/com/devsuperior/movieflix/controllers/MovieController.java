package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
	public ResponseEntity<Page<MovieCardDTO>> findAll(
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "10") Integer size,
		@RequestParam(defaultValue = "title") String sort,
		@RequestParam(required = false) Long genreId
	) {
		Page<MovieCardDTO> movies;

		if (genreId != null) {
			movies = movieService.findAllByGenreId(genreId, page, size, sort);
			return ResponseEntity.ok().body(movies);
		}

		movies = movieService.findAll(page, size, sort);

		return ResponseEntity.ok().body(movies);
	}

    @PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
	@GetMapping("/{id}")
	public ResponseEntity<MovieDetailsDTO> findById(@PathVariable Long id) {
		MovieDetailsDTO movie = movieService.findById(id);
		return ResponseEntity.ok(movie);
	}

}
