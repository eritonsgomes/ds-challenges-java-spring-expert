package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.factories.MovieFactory;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {

	@Mock
	private MovieRepository repository;
	
	@InjectMocks
	private MovieService service;

	private Long existingMovieId;
	private String movieTitle;
	private MovieEntity movieEntity;
	private MovieDTO movieDto;

	private Long nonExistingMovieId;
	private Long dependentMovieId;

	private Pageable pageable;
	private PageImpl<MovieEntity> movieEntityPage;

	@BeforeEach
	void setUp() throws Exception {
		existingMovieId = 1L;
		movieTitle = "Test Movie";

		dependentMovieId = 2L;
		nonExistingMovieId = 3L;

		pageable = PageRequest.of(0, 10);

		movieEntity = MovieFactory.createMovieEntity();
		movieDto = MovieFactory.createMovieDTO();
		movieEntityPage = new PageImpl<>(List.of(movieEntity));

		when(repository.existsById(existingMovieId)).thenReturn(Boolean.TRUE);
		when(repository.existsById(dependentMovieId)).thenReturn(Boolean.TRUE);
		when(repository.existsById(nonExistingMovieId)).thenReturn(Boolean.FALSE);

		when(repository.findById(existingMovieId)).thenReturn(Optional.of(movieEntity));
		when(repository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

		when(repository.save(any())).thenReturn(movieEntity);

		when(repository.getReferenceById(existingMovieId)).thenReturn(movieEntity);
		when(repository.getReferenceById(nonExistingMovieId)).thenThrow(EntityNotFoundException.class);

		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentMovieId);
	}

	@Nested
	@DisplayName("Find All Movies")
	class FindAllMovies {

		@Test
		public void findAllShouldReturnPagedMovieDTO() {
			when(repository.searchByTitle(movieTitle, pageable)).thenReturn(movieEntityPage);

			Pageable pageable = PageRequest.of(0, 10);

			Page<MovieDTO> result = service.findAll(movieTitle, pageable);

			assertNotNull(result);
			assertFalse(result.isEmpty());
			assertEquals(1L, result.getTotalElements());

			verify(repository, times(1)).searchByTitle(movieTitle, pageable);
		}

	}

	@Nested
	@DisplayName("Find Movie By ID")
	class FindMovieByID {

		@Test
		public void findByIdShouldReturnMovieDTOWhenIdExists() {
			MovieDTO result = service.findById(existingMovieId);

			assertNotNull(result);
			assertEquals(result.getId(), existingMovieId);
			assertEquals(result.getTitle(), movieTitle);

			verify(repository, times(1)).findById(existingMovieId);
		}

		@Test
		public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
			assertThrows(
				ResourceNotFoundException.class, () -> service.findById(nonExistingMovieId)
			);

			verify(repository, times(1)).findById(nonExistingMovieId);
		}

	}

	@Nested
	@DisplayName("Insert Movie")
	class InsertMovie {

		@Test
		public void insertShouldReturnMovieDTO() {
			movieDto = MovieFactory.createMovieDTO();

			MovieDTO result = service.insert(movieDto);

			assertNotNull(result);
			assertEquals(result.getTitle(), movieDto.getTitle());

			verify(repository, times(1)).save(any(MovieEntity.class));
		}

	}

	@Nested
	@DisplayName("Update Movie")
	class UpdateMovie {

		@Test
		public void updateShouldReturnMovieDTOWhenIdExists() {
			MovieDTO result = service.update(existingMovieId, movieDto);

			assertNotNull(result);
			assertEquals(result.getId(), movieDto.getId());
			assertEquals(result.getTitle(), movieDto.getTitle());

			verify(repository, times(1)).getReferenceById(existingMovieId);
			verify(repository, times(1)).save(any(MovieEntity.class));
		}

		@Test
		public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
			assertThrows(
				ResourceNotFoundException.class, () -> service.update(nonExistingMovieId, movieDto)
			);

			verify(repository, times(1)).getReferenceById(nonExistingMovieId);
		}

	}

	@Nested
	@DisplayName("Delete Movie")
	class DeleteMovie {

		@Test
		public void deleteShouldDoNothingWhenIdExists() {
			doNothing().when(repository).deleteById(existingMovieId);

			assertDoesNotThrow(() -> service.delete(existingMovieId));

			verify(repository, times(1)).existsById(existingMovieId);
			verify(repository, times(1)).deleteById(existingMovieId);
		}

		@Test
		public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
			assertThrows(
				ResourceNotFoundException.class, () -> service.delete(nonExistingMovieId)
			);

			verify(repository, times(1)).existsById(nonExistingMovieId);
		}

		@Test
		public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
			assertThrows(
				DatabaseException.class, () -> service.delete(dependentMovieId)
			);

			verify(repository, times(1)).existsById(dependentMovieId);
			verify(repository, times(1)).deleteById(dependentMovieId);
		}

	}

}
