package com.foodit.data.restaurant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * Simple Objectify test for Restaurant entity
 * 
 * @author Grant
 * 
 */
public class TestRestaurantDatabaseActions {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(TestRestaurantDatabaseActions.class);

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	/**
	 * Test saving, lookup and deleting for simple Restaurant entity
	 */
	@Test
	public void test_simple() {
		final Objectify ofy = ObjectifyService.ofy();

		final Restaurant restaurant = new Restaurant();
		restaurant.setName("name");

		ofy.save().entity(restaurant).now();
		final Long id = restaurant.getId();

		assertNotNull("Id is autogenerated", id);

		// Retrieve the restaurant
		Restaurant loadeRestaurant = ofy.load().type(Restaurant.class).id(id).now();

		assertEquals("Name", restaurant.getName(), loadeRestaurant.getName());

		// Change some data and save
		restaurant.setName("New Name");
		ofy.save().entity(restaurant).now();

		loadeRestaurant = ofy.load().type(Restaurant.class).id(id).now();

		assertEquals("Name Change", restaurant.getName(), loadeRestaurant.getName());

		// logger.info("Loaded Restaurant: " + loadeRestaurant);

		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();

		int count = ofy.load().type(Restaurant.class).count();

		assertEquals("Number Restaurants", count, listRestaurant.size());

		final Restaurant queryRestaurant = ofy.load().type(Restaurant.class).filter("name", "New Name").first().safe();
		assertNotNull("queryRestaurant", queryRestaurant);

		// QueryResultIterable<Restaurant> ths =
		// ofy.load().type(Restaurant.class).filter("name",
		// "New Name").iterable();
		//
		// for (Restaurant r1 : ths) {
		// logger.info("r1: " + r1);
		//
		// }

		// Delete it
		ofy.delete().entity(restaurant).now();
	}
}