package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dtos.responses.CategoryResponseDTO;
import com.devsuperior.dscatalog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
@RequiredArgsConstructor
public class CategoryResource {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<CategoryResponseDTO> allCategories = categoryService.findAll();
        return ResponseEntity.ok().body(allCategories);
    }

}
