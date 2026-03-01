package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.ProductEntity;
import com.devsuperior.dscatalog.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private ProductEntity product;
    private Long existingId;
    private Long nonExistingId;
    private Long coundTotalProducts;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        product = ProductFactory.createProductEntity();
        existingId = 1L;
        nonExistingId = 1000L;
        coundTotalProducts = 25L;
    }

    @Test
    void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
         product.setId(null);

         productRepository.save(product);

         Assertions.assertNotNull(product.getId());
         Assertions.assertEquals(coundTotalProducts + 1, product.getId());
    }

    @Test
    void deleteShouldDeleteRegistryWhenIdExists() {
        productRepository.deleteById(existingId);
        Optional<ProductEntity> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void deleteShouldNotDeleteRegistryWhenIdDoesNotExist() {
        boolean isProductNotFound = productRepository.existsById(nonExistingId);

        productRepository.deleteById(nonExistingId);
        Optional<ProductEntity> result = productRepository.findById(nonExistingId);

        Assertions.assertFalse(isProductNotFound);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void findByIdShouldReturnProductEntityWhenIdExists() {
        Optional<ProductEntity> result = productRepository.findById(existingId);

        ProductEntity entityFound = result.orElse(null);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertNotNull(entityFound, "Entity with id " + existingId + " not found");
    }

    @Test
    void findByIdShouldNotReturnProductEntityWhenIdDoesNotExist() {
        Optional<ProductEntity> result = productRepository.findById(nonExistingId);

        ProductEntity entityFound = result.orElse(null);

        Assertions.assertFalse(result.isPresent());
        Assertions.assertNull(entityFound, "Entity with id " + nonExistingId + " found");
    }

}
