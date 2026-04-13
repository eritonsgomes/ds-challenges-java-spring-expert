package com.devsuperior.dscommerce.projections;

public class CategoryMinProjectionImpl implements CategoryMinProjection {

    private Long id;
    private String name;

    public CategoryMinProjectionImpl() {
    }

    public CategoryMinProjectionImpl(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getName() {
        return "";
    }

}
