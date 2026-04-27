package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ScoreControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String adminToken, clientToken, invalidToken;
	private Long existingMovieId, nonExistingMovieId;

	private Map<String, Object> postScoreInstance;

	@BeforeEach
	public void setup() throws JSONException {
		baseURI = "http://localhost:8080";

		clientUsername = "alex@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";

		existingMovieId = 1L;
		nonExistingMovieId = 100L;

		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", existingMovieId);
		postScoreInstance.put("score", 4);

	}

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() {
		postScoreInstance.put("movieId", nonExistingMovieId);

		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());

	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() {

		postScoreInstance.put("movieId", null);

		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() {

		postScoreInstance.put("score", -4);

		JSONObject newScore = new JSONObject(postScoreInstance);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.body(newScore)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/scores")
		.then()
			.statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

	}

}
