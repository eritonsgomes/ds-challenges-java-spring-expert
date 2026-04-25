package com.devsuperior.dscommerce.controllers;

import com.devsuperior.dscommerce.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class OrderControllerE2ETests {
	
	private String clientUsername, clientPassword, adminUsername, adminPassword, adminOnlyUsername, adminOnlyPassword;
	private String clientToken, adminToken, adminOnlyToken, invalidToken;
	private Long existingOrderId, nonExistingOrderId;
	
	private Map<String, List<Map<String, Object>>> postOrderInstance;
	
	@BeforeEach
	public void setup() throws JSONException {
		baseURI = "http://localhost:8080";
		
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		adminOnlyUsername = "ana@gmail.com";
		adminOnlyPassword = "123456";
		
		existingOrderId = 1L;
		nonExistingOrderId = 100L;
		
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		adminOnlyToken = TokenUtil.obtainAccessToken(adminOnlyUsername, adminOnlyPassword);
		invalidToken = adminToken + "xpto";
		
		Map<String, Object> item1 = new HashMap<>();
		item1.put("productId", 1);
		item1.put("quantity", 2);
		
		Map<String, Object> item2 = new HashMap<>();
		item2.put("productId", 5);
		item2.put("quantity", 1);

		List<Map<String,Object>> itemInstances = new ArrayList<>();
		itemInstances.add(item1);
		itemInstances.add(item2);

		postOrderInstance = new HashMap<>();
		postOrderInstance.put("items", itemInstances);
	}

	@Nested
	@DisplayName("Find Order By Id")
	class FindByIdTests {

		@Test
		public void findByIdShouldReturnOrderWhenIdExistsAndAdminLogged() {
			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.accept(ContentType.JSON)
			.when()
				.get("/orders/{id}", existingOrderId)
			.then()
				.statusCode(200)
				.body("id", is(1))
				.body("moment", equalTo("2022-07-25T13:00:00Z"))
				.body("status", equalTo("PAID"))
				.body("client.name", equalTo("Alex Green"))
				.body("payment.moment", equalTo("2022-07-25T15:00:00Z"))
				.body("items.name", hasItems("The Lord of the Rings", "Macbook Pro"))
				.body("total", is(1431.0F));
		}

		@Test
		public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() {
			Long otherOrderId = 2L;

			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.accept(ContentType.JSON)
			.when()
				.get("/orders/{id}", otherOrderId)
			.then()
				.statusCode(200)
				.body("id", is(2))
				.body("moment", equalTo("2022-07-29T15:50:00Z"))
				.body("status", equalTo("DELIVERED"))
				.body("client.name", equalTo("Maria Brown"))
				.body("payment.moment", equalTo("2022-07-30T11:00:00Z"))
				.body("items.name", hasItems("Macbook Pro"))
				.body("total", is(1250.0F));
		}

		@Test
		public void findByIdShouldReturnForbiddenWhenIdExistsAndClientLoggedAndOrderDoesNotBelongUser() {
			Long otherOrderId = 1L;

			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.accept(ContentType.JSON)
			.when()
				.get("/orders/{id}", otherOrderId)
			.then()
				.statusCode(403);
		}

		@Test
		public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndAdminLogged() {
			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminToken)
				.accept(ContentType.JSON)
			.when()
				.get("/orders/{id}", nonExistingOrderId)
			.then()
				.statusCode(404);
		}

		@Test
		public void findByIdShouldReturnNotFoundWhenIdDoesNotExistAndClientLogged() {
			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.accept(ContentType.JSON)
			.when()
				.get("/orders/{id}", nonExistingOrderId)
			.then()
				.statusCode(404);
		}

		@Test
		public void findByIdShouldReturnUnauthorizedWhenIdExistsAndInvalidToken() {
			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + invalidToken)
				.accept(ContentType.JSON)
			.when()
				.get("/orders/{id}", existingOrderId)
			.then()
				.statusCode(401);
		}

	}

	@Nested
	@DisplayName("Insert Order")
	class InsertTests {

		@Test
		public void insertShouldReturnOrderCreatedWhenClientLogged() {

			JSONObject newOrder = new JSONObject(postOrderInstance);

			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
			.when()
				.post("/orders")
			.then()
				.statusCode(201)
				.body("status", equalTo("WAITING_PAYMENT"))
				.body("client.name", equalTo("Maria Brown"))
				.body("items.name", hasItems("The Lord of the Rings", "Rails for Dummies"))
				.body("total", is(281.99F));

		}

		@Test
		public void insertShouldReturnUnprocessableEntityWhenClientLoggedAndOrderHasNoItem() {

			postOrderInstance.put("items", null);
			JSONObject newOrder = new JSONObject(postOrderInstance);

			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
			.when()
				.post("/orders")
			.then()
				.statusCode(422);

		}

		@Test
		public void insertShouldReturnForbiddenWhenAdminLogged() {

			JSONObject newOrder = new JSONObject(postOrderInstance);

			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + adminOnlyToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
			.when()
				.post("/orders")
			.then()
				.statusCode(403);

		}

		@Test
		public void insertShouldReturnUnauthorizedWhenInvalidToken() {

			JSONObject newOrder = new JSONObject(postOrderInstance);

			given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + invalidToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newOrder)
			.when()
				.post("/orders")
			.then()
				.statusCode(401);

		}

	}

}
