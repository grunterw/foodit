package com.foodit.data.restaurant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodit.data.menu.Menus;
import com.foodit.data.order.Orders;
import com.google.appengine.labs.repackaged.com.google.common.io.Resources;

/**
 * tests the bulk loading of menus and orders and the serialisation into java
 * pojo classes from json
 * 
 * note: changes were made to the json data files to conform to the json spec
 * 
 * @author Grant
 * 
 */
public class TestLoadRestaurants {

	private final static Logger logger = LoggerFactory.getLogger(TestLoadRestaurants.class);

	ObjectMapper mapper = new ObjectMapper();

	List<DataFile> listDataFiles = new ArrayList<DataFile>();

	/**
	 * Load list with file prefix and expected results for each file
	 */
	@Before
	public void loadRestaurantStats() {
		listDataFiles.add(new DataFile("bbqgrill", 10, 86));
		listDataFiles.add(new DataFile("butlersthaicafe", 13, 98));
		listDataFiles.add(new DataFile("jashanexquisiteindianfood", 14, 104));
		listDataFiles.add(new DataFile("newchinaexpress", 20, 51));
	}

	/**
	 * loop through all data files loading menus and orders and checking against
	 * expected results
	 */
	@Test
	public void test_loadData() {
		try {

			for (DataFile dataFile : listDataFiles) {

				String ordersFilename = String.format("orders-%s.json", dataFile.getId());
				String menuFilename = String.format("menu-%s.json", dataFile.getId());

				/**
				 * load the menu for each restaurant
				 */

				URL menuResource = Resources.getResource(menuFilename);

				File menuFile = new File(menuResource.toURI());

				Menus menus = mapper.readValue(menuFile, Menus.class);

				assertEquals("Number of categories", dataFile.getNumberMenuCategories().intValue(), menus.getMenu().size());

				/**
				 * load the orders for each restaurant
				 */

				URL ordersResource = Resources.getResource(ordersFilename);

				File ordersFile = new File(ordersResource.toURI());

				//logger.info("Processing: " + ordersFile.getAbsolutePath());

				Orders orders = mapper.readValue(ordersFile, Orders.class);

				assertEquals("Number of Orders (" + dataFile.getId() + ")", dataFile.getNumberOrders().intValue(), orders.getOrders()
						.size());

				//logger.info(orders.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	/**
	 * Private class to help testing
	 * 
	 * @author Grant
	 * 
	 */
	private class DataFile {
		private final String id;
		private final Integer numberOrders;
		private final Integer numberMenuCategories;

		public DataFile(String id, Integer numberMenuCategories, Integer numberOrders) {
			super();
			this.id = id;
			this.numberOrders = numberOrders;
			this.numberMenuCategories = numberMenuCategories;
		}

		public String getId() {
			return id;
		}

		public Integer getNumberOrders() {
			return numberOrders;
		}

		public Integer getNumberMenuCategories() {
			return numberMenuCategories;
		}

	}
}
