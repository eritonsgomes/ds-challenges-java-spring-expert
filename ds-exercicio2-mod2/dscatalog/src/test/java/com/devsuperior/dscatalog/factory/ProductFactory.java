package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.entities.CategoryEntity;
import com.devsuperior.dscatalog.entities.ProductEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class ProductFactory {

    public static ProductEntity createProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Phone");
        productEntity.setDescription("Good Phone");
        productEntity.setDate(Instant.parse("2026-02-22T19:00:01.923Z"));
        productEntity.setPrice(800.00);
        productEntity.setImgURL("https://img.com/img.png");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(2L);
        categoryEntity.setName("Eletrônicos");

        productEntity.getCategories().add(categoryEntity);

        return productEntity;
    }

    public static ProductResponseDTO createProductResponseDto() {
        CategoryResponseDTO categoryResponseDto = CategoryFactory.createCategoryResponseDto();

        return ProductResponseDTO.builder()
                .id(1L)
                .name("Phone")
                .description("Good Phone")
                .date(Instant.parse("2026-02-22T19:00:01.923Z"))
                .price(800.00)
                .imgURL("https://img.com/img.png")
                .categories(Collections.singleton(categoryResponseDto))
                .build();
    }

    public static ProductRequestDTO createProductRequestDto() {
        CategoryRequestDTO categoryRequestDto = CategoryFactory.createCategoryRequestDto();

        return ProductRequestDTO.builder()
                .name("Phone")
                .description("Good Phone")
                .date(Instant.parse("2026-02-22T19:00:01.923Z"))
                .price(800.00)
                .imgURL("https://img.com/img.png")
                .categories(Collections.singleton(categoryRequestDto))
                .build();
    }

    public static ProductRequestDTO createProductRequestDtoWithCategories(Set<CategoryRequestDTO> categoryRequestDTOs) {
        ProductRequestDTO productMock = ProductFactory.createProductRequestDto();

        return ProductRequestDTO.builder()
                .name(productMock.name())
                .description(productMock.description())
                .date(productMock.date())
                .price(productMock.price())
                .imgURL(productMock.imgURL())
                .categories(categoryRequestDTOs)
                .build();
    }

}
