package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.factories.UserDetailsFactory;
import com.devsuperior.dsmovie.factories.UserFactory;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@Mock
	private CustomUserUtil userUtil;

	@Mock
	private UserRepository repository;

	@InjectMocks
	private UserService service;

	private String existingUsername, nonExistingUsername;
	private UserEntity userAdmin;
	private List<UserDetailsProjection> userDetails;

	@BeforeEach
	void setUp() throws Exception {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "user@gmail.com";

		userAdmin = UserFactory.createCustomAdminUser(1L, existingUsername);

		userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

		when(repository.searchUserAndRolesByUsername(existingUsername)).thenReturn(userDetails);
		when(repository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(new ArrayList<>());

		when(repository.findByUsername(existingUsername)).thenReturn(Optional.of(userAdmin));
		when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		when(userUtil.getLoggedUsername()).thenReturn(existingUsername);

		UserEntity result = service.authenticated();

		assertNotNull(result);
		assertEquals(result.getUsername(), existingUsername);
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		when(userUtil.getLoggedUsername()).thenReturn(nonExistingUsername);

		assertThrows(UsernameNotFoundException.class, () -> service.authenticated());
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUsername);

		assertNotNull(result);
		assertEquals(result.getUsername(), existingUsername);
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(nonExistingUsername));
	}

}
