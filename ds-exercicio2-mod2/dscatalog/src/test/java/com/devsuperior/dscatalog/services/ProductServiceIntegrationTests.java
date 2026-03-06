package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProductServiceIntegrationTests {

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 100L;
        countTotalProducts = 25L;
    }

    @Test
    void createShouldReturnProductResponseDTO() {
        ProductRequestDTO productRequestDTO = ProductFactory.createProductRequestDto();
        ProductResponseDTO productResponseDTO = productService.create(productRequestDTO);

        Assertions.assertNotNull(productResponseDTO);
        Assertions.assertNotNull(productResponseDTO.id());
        Assertions.assertFalse(productResponseDTO.categories().isEmpty());
        Assertions.assertNotNull(productResponseDTO.categories().stream().findFirst().get().id());
        Assertions.assertEquals(countTotalProducts + 1, productService.count());
    }

    @Test
    void createShouldThrowResourceNotFoundExceptionWhenCategoryNotFound() {
        CategoryRequestDTO categoryRequestDto = CategoryFactory.createCategoryRequestDtoWithInvalidId();

        ProductRequestDTO productRequestDto = ProductFactory.createProductRequestDtoWithCategories(
            Set.of(categoryRequestDto));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService
            .create(productRequestDto));

        Assertions.assertEquals(countTotalProducts, productService.count());
    }

    @Test
    void findByIdShouldReturnProductWhenIdExists() {
        ProductResponseDTO product = productService.findById(existingId);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(product.id(), existingId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService
            .findById(nonExistingId));
    }

    @Test
    void findAllPagesShouldReturnPages() {
        int pageNumber = 0;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<ProductResponseDTO> pages = productService.findAllPages(pageable);

        Assertions.assertFalse(pages.isEmpty());
        Assertions.assertEquals(pageNumber, pages.getNumber());
        Assertions.assertEquals(pageSize, pages.getSize());
        Assertions.assertEquals(pageSize, pages.getNumberOfElements());
        Assertions.assertEquals(countTotalProducts, pages.getTotalElements());
    }

    @Test
    void findAllPagesShouldReturnSortedPagesWhenSortByName() {
        int pageNumber = 0;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

        Page<ProductResponseDTO> pages = productService.findAllPages(pageable);

        Assertions.assertFalse(pages.isEmpty());
        Assertions.assertEquals(pageNumber, pages.getNumber());
        Assertions.assertEquals(pageSize, pages.getSize());
        Assertions.assertEquals(pageSize, pages.getNumberOfElements());
        Assertions.assertEquals(countTotalProducts, pages.getTotalElements());
        Assertions.assertTrue(pages.getSort().isSorted());
        Assertions.assertEquals("Macbook Pro", pages.getContent().get(0).name());
        Assertions.assertEquals("PC Gamer", pages.getContent().get(1).name());
        Assertions.assertEquals("PC Gamer Alfa", pages.getContent().get(2).name());
    }

    @Test
    void findAllPagesShouldReturnEmptyPagesWhenPageDoesNotExist() {
        int pageNumber = 10;
        int pageSize = 15;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<ProductResponseDTO> pages = productService.findAllPages(pageable);

        Assertions.assertTrue(pages.isEmpty());
        Assertions.assertEquals(pageNumber, pages.getNumber());
        Assertions.assertEquals(pageSize, pages.getSize());
        Assertions.assertEquals(Page.empty().getNumberOfElements(), pages.getNumberOfElements());
        Assertions.assertEquals(countTotalProducts, pages.getTotalElements());
    }

    @Test
    void updateShouldReturnProductResponseDtoWhenIdExists() {
        ProductRequestDTO productRequestDto = ProductFactory.createProductRequestDto();

        ProductResponseDTO productResponseDto = productService.update(existingId, productRequestDto);

        Assertions.assertEquals(countTotalProducts, productService.count());
        Assertions.assertNotNull(productResponseDto);
        Assertions.assertNotNull(productResponseDto.id());
        Assertions.assertEquals(existingId, productResponseDto.id());
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        ProductRequestDTO productRequestDto = ProductFactory.createProductRequestDto();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService
                .update(nonExistingId, productRequestDto));

        Assertions.assertEquals(countTotalProducts, productService.count());
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenCategoryNotFound() {
        CategoryRequestDTO categoryRequestDto = CategoryFactory.createCategoryRequestDtoWithInvalidId();

        ProductRequestDTO productRequestDto = ProductFactory.createProductRequestDtoWithCategories(
                Set.of(categoryRequestDto));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService
                .update(existingId, productRequestDto));

        Assertions.assertEquals(countTotalProducts, productService.count());
    }

    @Test
    void deleteShouldDeleteResourceWhenIdExists() {
        productService.deleteById(existingId);

        Assertions.assertEquals(countTotalProducts - 1, productService.count());
    }

    @Test
    void deleteShouldThrownResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(nonExistingId));

        Assertions.assertEquals(countTotalProducts, productService.count());
    }
}
