package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private ReviewRepository  reviewRepository;

	public ReviewDTO insert(@Valid ReviewDTO requestDto) {
		Review review = new Review();

		UserDTO dto = userService.getProfile();
		User user = userRepository.findByEmail(dto.getEmail());

		Movie movie = movieRepository.findById(requestDto.getMovieId()).orElseThrow(
			()->new IllegalArgumentException("Filme não encontrado")
		);

		review.setText(requestDto.getText());
		review.setUser(user);
		review.setMovie(movie);

		review = reviewRepository.save(review);

		return new ReviewDTO(review);
	}
}
