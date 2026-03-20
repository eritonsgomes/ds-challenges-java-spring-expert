package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dtos.requests.CategoryRequestDTO;
import com.devsuperior.dscatalog.dtos.requests.ProductRequestDTO;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.factory.ProductFactory;
import com.devsuperior.dscatalog.utils.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @Autowired
    private TokenUtil tokenUtil;

    private String username, password, bearerToken;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;

        username = "maria@gmail.com";
        password = "123456";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createShouldReturnProductResponseDTO() throws Exception {
        ProductRequestDTO productRequestDTO = ProductFactory.createProductRequestDto();

        String productJson = objectMapper.writeValueAsString(productRequestDTO);

        ResultActions result = mockMvc.perform(
            post("/products")
                .header("Authorization", "Bearer " + bearerToken)
                .content(productJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.id").value(countTotalProducts + 1));
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
        result.andExpect(jsonPath("$.categories").isArray());
        result.andExpect(jsonPath("$.categories").isNotEmpty());
    }

    @Test
    void createShouldReturnNotFoundWhenCategoryNotFound() throws Exception {
        CategoryRequestDTO categoryRequestDto = CategoryFactory.createCategoryRequestDtoWithInvalidId();

        ProductRequestDTO productRequestDto = ProductFactory.createProductRequestDtoWithCategories(
            Set.of(categoryRequestDto));

        String productJson = objectMapper.writeValueAsString(productRequestDto);

        ResultActions result = mockMvc.perform(
            post("/products")
                .header("Authorization", "Bearer " + bearerToken)
                .content(productJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void findAllShouldReturnProductResponseDtoList() throws Exception {
        ResultActions result = mockMvc.perform(
            get("/products")
            .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isArray());
    }

    @Test
    void findAllPagesShouldReturnSortedPagesWhenSortByName() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", String.valueOf(pageNumber));
        queryParams.add("size", String.valueOf(pageSize));
        queryParams.add("sort", "name,ASC");

        ResultActions result = mockMvc.perform(
                get("/products/pages")
                    .params(queryParams)
                    .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content").isArray());
        result.andExpect(jsonPath("$.content.[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content.[1].name").value("PC Gamer"));
        result.andExpect(jsonPath("$.content.[2].name").value("PC Gamer Alfa"));
        result.andExpect(jsonPath("$.page.size").value(pageSize));
        result.andExpect(jsonPath("$.page.number").value(pageNumber));
        result.andExpect(jsonPath("$.page.totalElements").value(countTotalProducts));
    }

    @Test
    void updateShouldReturnProductResponseDtoWhenIdExists() throws Exception {
        ProductRequestDTO productRequestDTO = ProductFactory.createProductRequestDto();

        String productJson = objectMapper.writeValueAsString(productRequestDTO);

        ResultActions result = mockMvc.perform(
                put("/products/{id}", existingId)
                    .header("Authorization", "Bearer " + bearerToken)
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
        ProductRequestDTO productRequestDTO = ProductFactory.createProductRequestDto();

        String productJson = objectMapper.writeValueAsString(productRequestDTO);

        ResultActions result = mockMvc.perform(
                put("/products/{id}", nonExistingId)
                    .header("Authorization", "Bearer " + bearerToken)
                    .content(productJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/products/{id}", existingId)
                    .header("Authorization", "Bearer " + bearerToken)
                    .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/products/{id}", nonExistingId)
                    .header("Authorization", "Bearer " + bearerToken)
                    .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

}
