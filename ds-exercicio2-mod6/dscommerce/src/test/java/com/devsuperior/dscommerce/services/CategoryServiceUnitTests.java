package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.CategoryMinDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.factories.CategoryFactory;
import com.devsuperior.dscommerce.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class CategoryServiceUnitTests {
	
	@InjectMocks
	private CategoryService service;
	
	@Mock
	private CategoryRepository repository;
	
	private Category category;

    @BeforeEach
	void setUp() {
		category = CategoryFactory.createCategory();

        List<Category> list = new ArrayList<>();
		list.add(category);
				
		Mockito.when(repository.findAll()).thenReturn(list);
	}
	
	@Test
	public void findAllShouldReturnListCategoryDTO() {
		
		List<CategoryMinDTO> result = service.findAll();
		
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(result.getFirst().getId(), category.getId());
		Assertions.assertEquals(result.getFirst().getName(), category.getName());
	}

}
