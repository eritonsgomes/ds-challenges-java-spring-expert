package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.factories.MovieFactory;
import com.devsuperior.dsmovie.factories.ScoreFactory;
import com.devsuperior.dsmovie.factories.UserFactory;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	@Mock
	private UserService  userService;

	@InjectMocks
	private ScoreService serviceService;

    private Long existingMovieId;
	private Long nonExistingMovieId;

	private ScoreEntity scoreEntity;
	private ScoreDTO scoreDto = ScoreFactory.createScoreDTO();

	@BeforeEach
	public void setUp() {
        UserEntity userEntity = UserFactory.createUserEntity();

		scoreEntity = ScoreFactory.createScoreEntity();
		scoreDto = ScoreFactory.createScoreDTO();

        MovieEntity movieEntity = MovieFactory.createMovieEntity();
		movieEntity.getScores().add(scoreEntity);

		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		when(userService.authenticated()).thenReturn(userEntity);

		when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
		when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

		when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
		when(movieRepository.save(any())).thenReturn(movieEntity);
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result = serviceService.saveScore(scoreDto);

		assertNotNull(result);
		assertEquals(result.getId(), existingMovieId);
		assertEquals(scoreDto.getMovieId(), existingMovieId);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		scoreDto = new ScoreDTO(nonExistingMovieId, scoreEntity.getValue());

		assertThrows(
			ResourceNotFoundException.class, () -> serviceService.saveScore(scoreDto)
		);

		verify(userService, times(1)).authenticated();
		verify(movieRepository, times(1)).findById(nonExistingMovieId);
	}

}
