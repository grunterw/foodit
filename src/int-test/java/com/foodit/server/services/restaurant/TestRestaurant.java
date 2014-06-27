package com.foodit.server.services.restaurant;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.foodit.data.restaurant.Restaurant;
import com.jayway.restassured.RestAssured;

/**
 * Integration test for Restaurants
 * 
 * @author Grant
 * 
 */
public class TestRestaurant {

	@Before
	/**
	 * Initialise RestAssured host details
	 * 
	 * @throws Exception
	 *             Any runtime Exception
	 */
	public void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
		RestAssured.basePath = "/api";
	}

	//@Test
	public void test_allRestaurants() {

		@SuppressWarnings("unchecked")
		List<Restaurant> listRestaurant = given().log().all().contentType("application/json").expect().log().all()
				.contentType("application/json").when().get("/restaurant/all").as(List.class);

		assertNotNull("List Restaurant", listRestaurant);

		assertEquals("Number of Restaurant", 4, listRestaurant.size());

	}

}
