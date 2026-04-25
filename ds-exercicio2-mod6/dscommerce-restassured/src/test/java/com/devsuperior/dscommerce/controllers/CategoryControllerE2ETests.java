package com.devsuperior.dscommerce.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;

public class CategoryControllerE2ETests {
	
	@BeforeEach
	public void setup() {
		baseURI = "http://localhost:8080";
	}

	@Nested
	@DisplayName("Find All Categories")
	class FindAllTests {

		@Test
		public void findAllShouldReturnListOfCategories() {

			given()
				.get("/categories")
			.then()
				.statusCode(200)
				.body("id", hasItems(1, 2, 3))
				.body("name", hasItems("Livros", "Eletrônicos", "Computadores"));

		}

	}

}
