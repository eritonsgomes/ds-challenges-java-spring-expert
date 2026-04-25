package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.factories.UserFactory;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.utils.TokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceUnitTests {

	@InjectMocks
	private AuthService service;

    @Mock
    private UserRepository userRepository;

    @Mock
	private TokenUtil tokenUtil;

    private String adminUserName, selfClientUserName, otherClientUserName;

    private User admin, selfClient, otherClient;
	
	@BeforeEach
	void setUp() {
        adminUserName = "alex@gmail.com";
        selfClientUserName = "bob@gmail.com";
        otherClientUserName = "ana@gmail.com";

		admin = UserFactory.createCustomClientUser(1L, adminUserName);
		selfClient = UserFactory.createCustomClientUser(2L, selfClientUserName);
		otherClient = UserFactory.createCustomClientUser(3L, otherClientUserName);
	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenAdminLogged() {

        when(tokenUtil.getUserNameFromToken()).thenReturn(adminUserName);
        when(userRepository.findByEmail(adminUserName)).thenReturn(Optional.of(admin));
		
		Long userId = admin.getId();
		
		Assertions.assertDoesNotThrow(() -> service.validateSelfOrAdmin(userId));
	}

	@Test
	public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
		
		when(tokenUtil.getUserNameFromToken()).thenReturn(selfClientUserName);
        when(userRepository.findByEmail(selfClientUserName)).thenReturn(Optional.of(selfClient));
		
		Long userId = selfClient.getId();
		
		Assertions.assertDoesNotThrow(() -> service.validateSelfOrAdmin(userId));
	}

	@Test
	public void validateSelfOrAdminThrowsForbiddenExceptionWhenClientOtherLogged() {
		
		when(tokenUtil.getUserNameFromToken()).thenReturn(selfClientUserName);
        when(userRepository.findByEmail(selfClientUserName)).thenReturn(Optional.of(selfClient));
		
		Long userId = otherClient.getId();
		
		Assertions.assertThrows(ForbiddenException.class, () -> service.validateSelfOrAdmin(userId));
	}

}
