package com.foodit.server.services.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodit.data.order.Item;
import com.foodit.data.order.Order;
import com.foodit.data.restaurant.AmountAnalysis;
import com.foodit.data.restaurant.OrderAnalysis;
import com.foodit.data.restaurant.Restaurant;
import com.foodit.data.restaurant.TestRestaurantDatabaseActions;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class TestAnalysis {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(TestRestaurantDatabaseActions.class);

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	private final Objectify ofy = ObjectifyService.ofy();

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void test_numberOrders_oneRestaurant_one_order() {

		Long restaurantId = insertRestaurant("one");

		// insert one order with one line item
		insertOrder(restaurantId, new Double[] { 9.99d });

		List<OrderAnalysis> listOrderAnalysis = AnalysisService.getNumberOrders();

		assertEquals("Only one Restaurant Record", 1, listOrderAnalysis.size());
		assertEquals("Only one Order", 1, listOrderAnalysis.get(0).getNumberOrders().intValue());

	}

	@Test
	public void test_numberOrders_oneRestaurant_two_order() {

		Long restaurantId = insertRestaurant("one");

		// insert one order with one line item
		insertOrder(restaurantId, new Double[] { 9.99d, 8.88d });
		insertOrder(restaurantId, new Double[] { 9.99d });

		List<OrderAnalysis> listOrderAnalysis = AnalysisService.getNumberOrders();

		assertEquals("Only one Restaurant Record", 1, listOrderAnalysis.size());
		assertEquals("Only one Order", 2, listOrderAnalysis.get(0).getNumberOrders().intValue());

	}

	@Test
	public void test_numberOrders_manyRestaurant_five_order() {

		/**
		 * Step 1 - add test data
		 */
		String[] restaurantNames = new String[] { "one", "two", "three", "four", "five" };
		for (String name : restaurantNames) {

			Long restaurantId = insertRestaurant(name);

			// 5 x insert one order with one line item per amount
			insertOrder(restaurantId, new Double[] { 9.99d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d, 7.77d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d, 7.77d, 6.66d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d, 7.77d, 6.66d, 5.55d });

		}

		/**
		 * Step 2 - Retrieve Number of Orders
		 */
		List<OrderAnalysis> listOrderAnalysis = AnalysisService.getNumberOrders();

		/**
		 * Step 3 - validate
		 */
		assertEquals("Many Restaurant Record", 5, listOrderAnalysis.size());

		for (OrderAnalysis orderAnalysis : listOrderAnalysis) {
			assertEquals("Five Orders per restaurant", 5, orderAnalysis.getNumberOrders().intValue());
		}

	}

	@Test
	public void test_totalAmount_oneRestaurant_one_order() {

		Long restaurantId = insertRestaurant("one");

		// insert one order with one line item
		insertOrder(restaurantId, new Double[] { 9.99d });

		List<AmountAnalysis> listAmountAnalysis = AnalysisService.getTotalAmount();

		assertEquals("Only one Restaurant Record", 1, listAmountAnalysis.size());
		assertEquals("Only one Order", 9.99d, listAmountAnalysis.get(0).getTotalAmount().doubleValue(),  0.01d);

	}

	@Test
	public void test_totalAmount_oneRestaurant_two_order() {

		Long restaurantId = insertRestaurant("one");

		// insert one order with one line item
		insertOrder(restaurantId, new Double[] { 9.99d, 8.88d });
		insertOrder(restaurantId, new Double[] { 9.99d });

		List<AmountAnalysis> listAmountAnalysis = AnalysisService.getTotalAmount();

		assertEquals("Only one Restaurant Record", 1, listAmountAnalysis.size());
		assertEquals("Only one Order", 28.86d, listAmountAnalysis.get(0).getTotalAmount().doubleValue(),  0.01d);

	}

	@Test
	public void test_totalAmount_manyRestaurant_five_order() {

		/**
		 * Step 1 - add test data
		 */
		String[] restaurantNames = new String[] { "one", "two", "three", "four", "five" };
		for (String name : restaurantNames) {

			Long restaurantId = insertRestaurant(name);

			// 5 x insert one order with one line item per amount
			insertOrder(restaurantId, new Double[] { 9.99d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d, 7.77d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d, 7.77d, 6.66d });
			insertOrder(restaurantId, new Double[] { 9.99d, 8.88d, 7.77d, 6.66d, 5.55d });

		}

		/**
		 * Step 2 - Retrieve Number of Orders
		 */
		List<AmountAnalysis> listAmountAnalysis = AnalysisService.getTotalAmount();

		/**
		 * Step 3 - validate
		 */
		assertEquals("Many Restaurant Record", 5, listAmountAnalysis.size());

		for (AmountAnalysis amountAnalysis : listAmountAnalysis) {
			assertEquals("Five Orders per restaurant", 127.65d, amountAnalysis.getTotalAmount().doubleValue(), 0.01d);
		}

	}

	/**
	 * insert test order data - a line item for each element in itemAmounts
	 * array
	 * 
	 * @param restaurantId
	 *            The id of the restaurant
	 * @param itemAmounts
	 */
	private void insertOrder(Long restaurantId, Double itemAmounts[]) {

		final Order order = new Order();
		order.setRestaurantId(restaurantId);

		List<Item> listItem = new ArrayList<Item>();
		double totalAmt = 0d;
		for (Double amount : itemAmounts) {
			Item item = new Item();

			item.setTotal(amount);
			listItem.add(item);
			totalAmt += amount.doubleValue();
		}

		order.setLineItems(listItem);
		order.setTotalValue(totalAmt);

		ofy.save().entity(order).now();

	}

	/**
	 * inserts a test restaurant record
	 * 
	 * @param name
	 *            name of the restaurant
	 * @return DB ID
	 */
	private Long insertRestaurant(String name) {
		final Restaurant restaurant = new Restaurant();
		restaurant.setName(name);

		ofy.save().entity(restaurant).now();
		final Long id = restaurant.getId();

		assertNotNull("Id is autogenerated", id);

		return id;

	}
}
