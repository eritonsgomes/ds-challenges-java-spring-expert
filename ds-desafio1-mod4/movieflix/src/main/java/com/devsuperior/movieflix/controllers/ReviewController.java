package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@PreAuthorize("hasAnyRole('ROLE_MEMBER')")
	@PostMapping
	public ResponseEntity<ReviewDTO> insert(@Valid @RequestBody ReviewDTO requestDto) {
		ReviewDTO review = reviewService.insert(requestDto);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(review.getId()).toUri();

		return ResponseEntity.created(uri).body(review);
	}

}
