package com.foodit.server.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodit.data.menu.Menu;
import com.foodit.data.menu.Menus;
import com.foodit.data.order.Order;
import com.foodit.data.order.Orders;
import com.foodit.data.restaurant.Restaurant;
import com.google.appengine.labs.repackaged.com.google.common.io.Resources;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * A singleton of the Main Application
 * 
 * @author Grant
 * 
 */
public class MainApplication {
	private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

	private static MainApplication instance;

	private ServletContext servletContext;

	private final ObjectMapper mapper = new ObjectMapper();

	private final Objectify ofy = ObjectifyService.ofy();

	/**
	 * getter for servlet context
	 * 
	 * @return the servlet context
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * get singleton of the main application
	 * 
	 * @return a singleton of main application
	 */
	public static synchronized MainApplication getInstance() {
		if (instance == null) {
			instance = new MainApplication();
		}
		return instance;
	}

	/**
	 * Initialise the main application
	 * 
	 * @param servletContext
	 *            The servlet context
	 * @throws Exception
	 *             any runtime exception
	 */
	public void init(ServletContext servletContext) throws Exception {

		try {
			this.servletContext = servletContext;

			logger.info("\n\n\t\t------------------ Initialising Main Application -----------------\n");

			registerEntities();

			// Check if DB has data
			if (!checkHaveData()) {
				loadData();
			}

			showDBStatistics();

			logger.info("\n\n\t\t------------------ Successfully Loaded Main Application -----------------\n ");

		} catch (Exception e) {
			logger.error("------------------ Failed to load Main Application Error: " + e.getMessage() + " ----------------- ", e);
			throw e;
		}

	}

	/**
	 * slight quirk - currently the register event is a static within the pojo -
	 * but it is possible to use the DB without initialising the class and hence
	 * registering the entity - so we have to do it manually
	 */
	private void registerEntities() {
		ObjectifyService.register(Restaurant.class);
		ObjectifyService.register(Menu.class);
		ObjectifyService.register(Order.class);

	}

	private void showDBStatistics() {
		logger.info("\n\t\t\t--------- data statistics ---------\n ");

		logger.info("No. Restaurants:       " + ofy.load().type(Restaurant.class).list().size());
		logger.info("No. Menu (categories): " + ofy.load().type(Menu.class).list().size());
		logger.info("No. orders:            " + ofy.load().type(Order.class).list().size());

//		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();
//		for (Restaurant restaurant : listRestaurant) {
//			logger.info("Restaurant: " + restaurant);
//		}

	}

	/**
	 * Checks if there is data loaded in the database. This is done by checking
	 * if there are any Restaurant records
	 * 
	 * @return boolean true is there is data and false if no data
	 * @throws Exception
	 */
	private boolean checkHaveData() throws Exception {

		final Objectify ofy = ObjectifyService.ofy();

		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();

		return listRestaurant.size() == 0 ? false : true;
	}

	/**
	 * loads data from static json files
	 * 
	 * @throws Exception
	 */
	private void loadData() throws Exception {
		logger.info("\n\t\t\t--------- loading new data ---------\n ");

		String[] restaurants = new String[] { "bbqgrill", "butlersthaicafe", "jashanexquisiteindianfood", "newchinaexpress" };

		for (String restaurantTag : restaurants) {
			logger.info("Loading data for: " + restaurantTag);
			loadRestaurantData(restaurantTag);
		}
	}

	private void loadRestaurantData(String restaurantTag) throws Exception {

		/**
		 * Step 1 - create a restaurant entity
		 */
		final Restaurant restaurant = new Restaurant();
		restaurant.setName(restaurantTag);

		ofy.save().entity(restaurant).now();
		final Long restaurantId = restaurant.getId();

		/**
		 * Step 2 - link menu data to restaurant
		 */

		saveMenuData(restaurantTag, restaurantId);

		/**
		 * Step 3 - link order data to restaurant
		 */

		saveOrderData(restaurantTag, restaurantId);

	}

	/**
	 * Reads menu data from a json file and saves to be for each restaurant
	 * 
	 * @param restaurantTag
	 *            The tag / name of the restaurant
	 * @param restaurantId
	 *            the DB record Id for this restaurant
	 * @throws Exception
	 */
	private void saveMenuData(String restaurantTag, Long restaurantId) throws Exception {

		String menuFilename = String.format("menu-%s.json", restaurantTag);

		/**
		 * Step 1 - load the resource
		 */

		URL menuResource = Resources.getResource(menuFilename);
		File menuFile = new File(menuResource.toURI());

		logger.info("Processing Menu File: " + menuFile.getAbsolutePath());

		Menus menus = mapper.readValue(menuFile, Menus.class);

		/**
		 * Step 2 - loop through menus and add to DB
		 */

		List<Menu> listMenu = new ArrayList<Menu>();

		for (Menu menu : menus.getMenu()) {

			menu.setRestaurantId(restaurantId);
			listMenu.add(menu);

		}

		ofy.save().entities(listMenu).now();

	}

	/**
	 * Reads order data from a json file and saves to be for each restaurant
	 * 
	 * @param restaurantTag
	 *            The tag / name of the restaurant
	 * @param restaurantId
	 *            the DB record Id for this restaurant
	 * @throws Exception
	 */
	private void saveOrderData(String restaurantTag, Long restaurantId) throws Exception {

		String ordersFilename = String.format("orders-%s.json", restaurantTag);

		/**
		 * Step 1 - load the resource
		 */
		URL ordersResource = Resources.getResource(ordersFilename);
		File ordersFile = new File(ordersResource.toURI());

		logger.info("Processing Order File: " + ordersFile.getAbsolutePath());

		/**
		 * Step 2 - loop through orders and add to DB - one order per record
		 */
		Orders orders = mapper.readValue(ordersFile, Orders.class);

		List<Order> listOrder = new ArrayList<Order>();

		for (Order order : orders.getOrders()) {

			order.setRestaurantId(restaurantId);
			listOrder.add(order);

		}
		ofy.save().entities(listOrder).now();

	}
}
