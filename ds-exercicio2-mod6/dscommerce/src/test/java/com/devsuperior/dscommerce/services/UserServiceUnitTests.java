package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.factories.UserDetailsFactory;
import com.devsuperior.dscommerce.factories.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceUnitTests {

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private String existingUsername, nonExistingUsername;
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() {
        existingUsername = "maria@gmail.com";
        nonExistingUsername = "user@gmail.com";

        user = UserFactory.createCustomClientUser(1L, existingUsername);

        userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

        when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);
        when(repository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(new ArrayList<>());

        when(repository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
        when(repository.findByEmail(nonExistingUsername)).thenReturn(Optional.empty());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {

        UserDetails result = service.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUsername);
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {

        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(nonExistingUsername));
    }

    @Test
    public void getAuthenticatedUserShouldReturnUserWhenUserExists() {

        when(authService.getAuthenticatedUserName()).thenReturn(existingUsername);

        User result = service.getAuthenticatedUser();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUsername);
    }

    @Test
    public void getAuthenticatedUserShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {

        when(authService.getAuthenticatedUserName()).thenReturn(nonExistingUsername);

        Assertions.assertThrows(UsernameNotFoundException.class, service::getAuthenticatedUser);
    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserAuthenticated() {
        when(authService.getAuthenticatedUserName()).thenReturn(existingUsername);

        UserDTO result = service.getMe();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getEmail(), existingUsername);
    }

    @Test
    public void getMeShouldThrowUsernameNotFoundExceptionWhenUserNotAuthenticated() {
        UserService spyUserService = spy(service);

        doThrow(UsernameNotFoundException.class).when(spyUserService).getMe();

        Assertions.assertThrows(UsernameNotFoundException.class, spyUserService::getMe);
    }

}
