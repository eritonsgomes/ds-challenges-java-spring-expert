package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;
import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.exceptions.database.DatabaseException;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.factory.ProductFactory;
import com.devsuperior.dscatalog.mappers.ProductResponseDTOMapper;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTests {

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductEntity product;
    private PageImpl<ProductEntity> productEntityPage;
    private ProductRequestDTO productRequestDto;
    private ProductResponseDTO productResponseDto;
    private CategoryEntity category;
    private CategoryRequestDTO categoryRequestDto;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        product = ProductFactory.createProductEntity();
        productEntityPage = new PageImpl<>(List.of(product));
        productResponseDto = ProductFactory.createProductResponseDto();
        productRequestDto = ProductFactory.createProductRequestDto();
        category = CategoryFactory.createCategoryEntity();
        categoryRequestDto = CategoryFactory.createCategoryRequestDto();
    }

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductResponseDTOMapper productResponseMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void findByIdShouldReturnProductResponseDtoWhenIdExists() {
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productResponseMapper.toDTO(product)).thenReturn(productResponseDto);

        ProductResponseDTO productResponseDTO = productService.findById(existingId);

        Assertions.assertNotNull(productResponseDTO);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findById(nonExistingId));

        verify(productRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void updateShouldReturnProductResponseDtoWhenIdExists() {
        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.save(any())).thenReturn(product);
        when(productResponseMapper.toDTO(product)).thenReturn(productResponseDto);
        when(categoryRepository.findById(categoryRequestDto.id())).thenReturn(Optional.ofNullable(category));
        when(categoryRepository.existsById(categoryRequestDto.id())).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);

        ProductResponseDTO productResponseDTO = productService.update(existingId, productRequestDto);

        verify(productRepository, times(1)).existsById(existingId);
        verify(productRepository, times(1)).getReferenceById(existingId);
        verify(productRepository, times(1)).save(product);

        Assertions.assertNotNull(productResponseDTO);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService
                .update(nonExistingId, productRequestDto));

        verify(productRepository, times(1)).existsById(nonExistingId);
    }

    @Test
    void deleteShouldNotDeleteProductWhenIdNonExists() {
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(nonExistingId));

        verify(productRepository, times(1)).existsById(nonExistingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.deleteById(nonExistingId));

        verify(productRepository, times(1)).existsById(nonExistingId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        when(productRepository.existsById(dependentId)).thenReturn(true);
        doThrow(DatabaseException.class).when(productRepository).deleteById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> productService.deleteById(dependentId));

        verify(productRepository, times(1)).existsById(dependentId);
    }

    @Test
    void deleteShouldDoNothingWhenProductIdExists() {
        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);

        Assertions.assertDoesNotThrow(() -> productService.deleteById(existingId));

        verify(productRepository, times(1)).existsById(existingId);
        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    void findAllPagesShouldReturnPage() {
        when(productResponseMapper.toDTO(product)).thenReturn(productResponseDto);
        when(productRepository.findAll((Pageable) any())).thenReturn(productEntityPage);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductResponseDTO> result = productService.findAllPages(pageable);

        Assertions.assertNotNull(result);
        verify(productRepository, times(1)).findAll(pageable);
    }

}
