package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.factories.OrderFactory;
import com.devsuperior.dscommerce.factories.ProductFactory;
import com.devsuperior.dscommerce.factories.UserFactory;
import com.devsuperior.dscommerce.projections.OrderProjection;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTests {

	@InjectMocks
	private OrderService service;
	
	@Mock
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private UserService userService;
	
	private Long existingOrderId, nonExistingOrderId;
	private Long existingProductId, nonExistingProductId;
	private Order adminOrder;
	private Order clientOrder;
	private OrderDTO orderDTO;
	private User admin, client;
	private Product product;
	private List<OrderProjection> orderProjections;

	@BeforeEach
	void setUp() throws Exception {
		existingOrderId = 1L;
		nonExistingOrderId = 1000L;

		existingProductId = 1L;
		nonExistingProductId = 1000L;

		admin = UserFactory.createCustomAdminUser(1L, "admin@email.com");
		client = UserFactory.createCustomClientUser(2L, "client@email.com");

		clientOrder = OrderFactory.createOrder(client);
		adminOrder = OrderFactory.createOrder(admin);
		orderProjections = OrderFactory.createOrderProjection(admin);

		orderDTO = new OrderDTO(clientOrder);

		product = ProductFactory.createProduct();
		product.setId(existingProductId);

		when(repository.findById(existingOrderId)).thenReturn(Optional.of(clientOrder));
		when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

		when(repository.searchById(existingOrderId)).thenReturn(orderProjections);

		when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
		when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

		when(repository.save(any())).thenReturn(clientOrder);
		when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(clientOrder.getItems()));
	}

	@Test
	public void findByIdShouldReturnOrderWhenIdExists() {

		Order result = service.findById(existingOrderId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);

	}

	@Test
	public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(
			ResourceNotFoundException.class,
			() -> service.findById(nonExistingOrderId)
		);

	}

	@Test
	public void searchByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged() {

		OrderService spyService = Mockito.spy(service);

		Mockito.when(repository.searchById(existingOrderId)).thenReturn(orderProjections);
		Mockito.when(userService.getAuthenticatedUser()).thenReturn(admin);
		Mockito.doNothing().when(spyService).validateAccessPermission(any(), eq(admin));

		OrderDTO result = service.searchById(existingOrderId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);
		Assertions.assertEquals(result.getClient().getId(), admin.getId());

	}

	@Test
	public void searchByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {

		OrderService spyService = Mockito.spy(service);

		Mockito.when(repository.searchById(existingOrderId)).thenReturn(orderProjections);
		Mockito.when(userService.getAuthenticatedUser()).thenReturn(admin);
		Mockito.doNothing().when(spyService).validateAccessPermission(any(), eq(admin));

		OrderDTO result = service.searchById(existingOrderId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);
		Assertions.assertEquals(result.getClient().getId(), admin.getId());

	}

	@Test
	public void searchByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {

		OrderService spyService = Mockito.spy(service);

		Mockito.when(repository.searchById(existingOrderId)).thenReturn(orderProjections);
		Mockito.when(userService.getAuthenticatedUser()).thenReturn(client);

		Assertions.assertThrows(ForbiddenException.class, () -> spyService.searchById(existingOrderId));

	}

	@Test
	public void searchByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(
			ResourceNotFoundException.class,
			() -> service.searchById(nonExistingOrderId)
		);
	}

	@Test
	public void insertShouldReturnOrderDTOWhenAdminLogged() {

		when(repository.save(any())).thenReturn(adminOrder);
		when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(adminOrder.getItems()));
		when(userService.getAuthenticatedUser()).thenReturn(admin);
		
		OrderDTO result = service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), adminOrder.getId());

		Assertions.assertNotNull(result.getClient());
		Assertions.assertEquals(result.getClient().getId(), admin.getId());
		Assertions.assertEquals(result.getClient().getName(), admin.getName());

	}
	
	@Test
	public void insertShouldReturnOrderDTOWhenClientLogged() {

		when(repository.save(any())).thenReturn(clientOrder);
		when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(clientOrder.getItems()));
		when(userService.getAuthenticatedUser()).thenReturn(client);

		OrderDTO result = service.insert(orderDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), clientOrder.getId());

		Assertions.assertNotNull(result.getClient());
		Assertions.assertEquals(result.getClient().getId(), client.getId());
		Assertions.assertEquals(result.getClient().getName(), client.getName());

	}
	
	@Test
	public void insertShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {

		Mockito.doThrow(UsernameNotFoundException.class).when(userService).getAuthenticatedUser();
		
		clientOrder.setClient(new User());
		orderDTO = new OrderDTO(clientOrder);
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> service.insert(orderDTO));

	}
	
	@Test
	public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductIdDoesNotExist() {

		when(userService.getAuthenticatedUser()).thenReturn(client);
		when(repository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);
		
		product.setId(nonExistingProductId);

		OrderItem orderItem = new OrderItem(clientOrder, product, 2, 10.0);
		clientOrder.getItems().clear();
		clientOrder.getItems().add(orderItem);
		
		orderDTO = new OrderDTO(clientOrder);
		
		Assertions.assertThrows(EntityNotFoundException.class, () -> service.insert(orderDTO));

	}

}
