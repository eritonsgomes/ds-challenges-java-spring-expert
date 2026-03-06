package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dtos.responses.ProductResponseDTO;
import com.devsuperior.dscatalog.exceptions.database.DatabaseException;
import com.devsuperior.dscatalog.exceptions.services.ResourceNotFoundException;
import com.devsuperior.dscatalog.factory.ProductFactory;
import com.devsuperior.dscatalog.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @Autowired
    private ObjectMapper objectMapper;

    private PageImpl<ProductResponseDTO> productResponseDtoPage;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        ProductResponseDTO productResponseDTO = ProductFactory.createProductResponseDto();
        productResponseDtoPage = new PageImpl<>(List.of(productResponseDTO));
    }

    @Test
    void findAllPagesShouldReturnPages() throws Exception {
        when(productService.findAllPages(any())).thenReturn(productResponseDtoPage);

        ResultActions result = mockMvc.perform(
            get("/products/pages")
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductResponseDtoWhenIdExists() throws Exception {
        ProductResponseDTO productResponseDTO = ProductFactory.createProductResponseDto();
        when(productService.findById(existingId)).thenReturn(productResponseDTO);

        ResultActions result = mockMvc.perform(
                get("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        ResultActions result = mockMvc.perform(
                get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnProductResponseDtoWhenIdExists() throws Exception {
        ProductResponseDTO productResponseDTO = ProductFactory.createProductResponseDto();
        when(productService.update(eq(existingId), any())).thenReturn(productResponseDTO);

        String productJson = objectMapper.writeValueAsString(productResponseDTO);

        ResultActions result = mockMvc.perform(
            put("/products/{id}", existingId)
                .content(productJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.categories").isArray());
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ProductResponseDTO productResponseDTO = ProductFactory.createProductResponseDto();
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        String productJson = objectMapper.writeValueAsString(productResponseDTO);

        ResultActions result = mockMvc.perform(
            put("/products/{id}", nonExistingId)
                .content(productJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        doNothing().when(productService).deleteById(existingId);

        ResultActions result = mockMvc.perform(
                delete("/products/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        doThrow(ResourceNotFoundException.class).when(productService).deleteById(nonExistingId);

        ResultActions result = mockMvc.perform(
                delete("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        doThrow(DatabaseException.class).when(productService).deleteById(dependentId);

        ResultActions result = mockMvc.perform(
                delete("/products/{id}", dependentId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest());
    }

}
