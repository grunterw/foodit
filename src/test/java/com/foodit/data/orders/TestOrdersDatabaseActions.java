package com.foodit.data.orders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodit.data.menu.TestMenusDatabaseActions;
import com.foodit.data.order.Order;
import com.foodit.data.order.Orders;
import com.foodit.data.restaurant.Restaurant;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.io.Resources;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class TestOrdersDatabaseActions {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(TestMenusDatabaseActions.class);

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	/**
	 * test loading order data from json file and inserting into database
	 */
	@Test
	public void test_addLoadedToRestaurant() {
		final Objectify ofy = ObjectifyService.ofy();

		/**
		 * Step 1 - create a parent restaurant entity
		 */
		final Restaurant restaurant = new Restaurant();
		restaurant.setName("name");

		ofy.save().entity(restaurant).now();
		final Long restaurantId = restaurant.getId();

		assertNotNull("Restaurant Id is autogenerated", restaurantId);

		/**
		 * Step 2 - Read orders from file
		 */
		final Orders orders = loadPojoFromFile();

		/**
		 * Step 3 - Each record is entered as a separate record
		 */
		for (Order order : orders.getOrders()) {

			order.setRestaurantId(restaurantId);
			ofy.save().entity(order).now();

		}

		/**
		 * Step 4 - Check DB entries
		 */
		List<Order> listOrder = ofy.load().type(Order.class).list();

		assertEquals("No. Orders", 86, listOrder.size());

		listOrder = ofy.load().type(Order.class).filter("restaurantId", restaurantId).list();

		assertEquals("No. orders when filtered", 86, listOrder.size());

	}

	/**
	 * loads menu from small json file
	 * 
	 * @return
	 */
	private Orders loadPojoFromFile() {
		try {
			URL jsonResource = Resources.getResource("AllOrders.json");
			File jsonFile = new File(jsonResource.toURI());

			Orders orders = mapper.readValue(jsonFile, Orders.class);

			return orders;
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
			return null;
		}

	}

}
