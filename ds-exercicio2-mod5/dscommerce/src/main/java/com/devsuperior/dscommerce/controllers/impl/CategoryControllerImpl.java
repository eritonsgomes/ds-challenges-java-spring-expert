package com.devsuperior.dscommerce.controllers.impl;

import com.devsuperior.dscommerce.controllers.CategoryController;
import com.devsuperior.dscommerce.dto.CategoryMinDTO;
import com.devsuperior.dscommerce.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryControllerImpl implements CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<List<CategoryMinDTO>> findAll() {
        List<CategoryMinDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

}
