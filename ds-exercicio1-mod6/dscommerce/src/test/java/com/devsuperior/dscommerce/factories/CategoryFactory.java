package com.devsuperior.dscommerce.factories;

import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.projections.CategoryMinProjection;
import com.devsuperior.dscommerce.projections.CategoryMinProjectionImpl;

import java.util.ArrayList;
import java.util.List;

public class CategoryFactory {
	
	public static Category createCategory() {
		return new Category(1L, "Games");
	}
	
	public static Category createCategory(Long id, String name) {
		return new Category(id, name);
	}

	public static List<CategoryMinProjection>  createCategoryMinProjections() {
		List<CategoryMinProjection> categories = new ArrayList<>();

		categories.add(new CategoryMinProjectionImpl(1L, "Livros"));
		categories.add(new CategoryMinProjectionImpl(2L, "Eletrônicos"));
		categories.add(new CategoryMinProjectionImpl(3L, "Computadores"));

		return categories;
	}

}
