package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.projections.CategoryMinProjection;
import com.devsuperior.dscommerce.repositories.CategoryRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscommerce.factories.CategoryFactory;
import com.devsuperior.dscommerce.factories.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceUnitTests {
	
	@InjectMocks
	private ProductService service;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private ProductRepository repository;
	
	private long existingProductId, nonExistingProductId, dependentProductId;
	private String productName;
	private Product product;
	private ProductDTO productDTO;

    @BeforeEach
	void setUp() {
		existingProductId = 1L;
		nonExistingProductId = 2L;
		dependentProductId = 3L;
		
		productName = "PlayStation 5";

        List<CategoryMinProjection> categories = CategoryFactory.createCategoryMinProjections();

		when(categoryRepository.searchByIdIn(any())).thenReturn(categories);
		
		product = ProductFactory.createProduct(productName);
		productDTO = new ProductDTO(product);
        PageImpl<Product> page = new PageImpl<>(List.of(product));
		
		when(repository.findById(existingProductId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingProductId)).thenReturn(Optional.empty());
		
		when(repository.searchByName(any(), any())).thenReturn(page);
		
		when(repository.save(any())).thenReturn(product);
		
		when(repository.getReferenceById(existingProductId)).thenReturn(product);
		when(repository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);
		
		when(repository.existsById(existingProductId)).thenReturn(true);
		when(repository.existsById(dependentProductId)).thenReturn(true);
		when(repository.existsById(nonExistingProductId)).thenReturn(false);
		
		doNothing().when(repository).deleteById(existingProductId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentProductId);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.findById(existingProductId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingProductId);
		Assertions.assertEquals(result.getName(), product.getName());
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingProductId));
	}
	
	@Test
	public void findAllShouldReturnPagedProductMinDTO() {
		
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<ProductMinDTO> result = service.findAll(productName, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getSize());
		Assertions.assertEquals(result.iterator().next().getName(), productName);
	}
	
	@Test
	public void insertShouldReturnProductDTO() {
		
		ProductDTO result = service.insert(productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), product.getId());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO result = service.update(existingProductId, productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingProductId);
		Assertions.assertEquals(result.getName(), productDTO.getName());
	}
	
	@Test
	public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingProductId, productDTO));
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> service.delete(existingProductId));
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingProductId));
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(DatabaseException.class, () -> service.delete(dependentProductId));
	}
}
