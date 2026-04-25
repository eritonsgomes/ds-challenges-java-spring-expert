package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.projections.CategoryMinProjection;

public class CategoryMinDTO {

    private Long id;
    private String name;

    public CategoryMinDTO() {
        super();
    }

    public CategoryMinDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryMinDTO(Category category) {
        id = category.getId();
        name = category.getName();
    }

    public CategoryMinDTO(CategoryMinProjection projection) {
        id = projection.getId();
        name = projection.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
