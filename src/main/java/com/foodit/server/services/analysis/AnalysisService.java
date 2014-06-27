package com.foodit.server.services.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodit.data.menu.Menu;
import com.foodit.data.menu.MenuItem;
import com.foodit.data.order.Item;
import com.foodit.data.order.Order;
import com.foodit.data.restaurant.AmountAnalysis;
import com.foodit.data.restaurant.CategoryCounter;
import com.foodit.data.restaurant.FrequentCategory;
import com.foodit.data.restaurant.FrequentMeals;
import com.foodit.data.restaurant.OrderAnalysis;
import com.foodit.data.restaurant.Restaurant;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class AnalysisService {

	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(AnalysisService.class);

	/**
	 * Retrieves the number of orders for each restaurant
	 * 
	 * @return
	 */
	public static List<OrderAnalysis> getNumberOrders() {

		final Objectify ofy = ObjectifyService.ofy();

		List<OrderAnalysis> listOrderAnalysis = new ArrayList<OrderAnalysis>();

		// retrieve list of all restaurants
		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();

		// loop through all restaurants
		for (Restaurant restaurant : listRestaurant) {

			// count the number of orders for the restaurant
			int numOrders = ofy.load().type(Order.class).filter("restaurantId", restaurant.getId()).list().size();

			// create an order analysis pojo and add to list
			OrderAnalysis orderAnalysis = new OrderAnalysis(restaurant.getId(), restaurant.getName(), numOrders);
			listOrderAnalysis.add(orderAnalysis);

		}

		return listOrderAnalysis;
	}

	/**
	 * retrieves total amount of sales per restaurant
	 * 
	 * @return
	 */
	public static List<AmountAnalysis> getTotalAmount() {
		final Objectify ofy = ObjectifyService.ofy();

		List<AmountAnalysis> listAmountAnalysis = new ArrayList<AmountAnalysis>();

		// retrieve list of all restaurants
		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();

		// loop through all restaurants
		for (Restaurant restaurant : listRestaurant) {

			// count the number of orders for the restaurant
			List<Order> listOrder = ofy.load().type(Order.class).filter("restaurantId", restaurant.getId()).list();

			double totalAmount = 0d;
			for (Order order : listOrder) {
				totalAmount += order.getTotalValue().doubleValue();
			}

			// create an amount analysis pojo and add to list
			AmountAnalysis amountAnalysis = new AmountAnalysis(restaurant.getId(), restaurant.getName(), totalAmount);
			listAmountAnalysis.add(amountAnalysis);

		}

		return listAmountAnalysis;
	}

	public static List<FrequentMeals> getFrequentMeals() {
		return calculateFrequentMeals(10);
	}

	public static List<FrequentMeals> getFrequentMeals(Integer count) {
		return calculateFrequentMeals(count);
	}

	public static List<FrequentCategory> getFrequentCategory() {
		return calculateFrequentCategory();
	}

	/**
	 * returns list of most frequent meals
	 * 
	 * @param limit
	 *            the number that should be returned
	 * @return
	 */
	private static List<FrequentMeals> calculateFrequentMeals(int limit) {

		final Objectify ofy = ObjectifyService.ofy();

		ObjectifyService.register(Restaurant.class);
		ObjectifyService.register(Order.class);
		ObjectifyService.register(Menu.class);

		Map<FrequentMeals, FrequentMeals> mapTotals = new HashMap<FrequentMeals, FrequentMeals>();

		List<Order> listOrder = ofy.load().type(Order.class).list();
		for (Order order : listOrder) {
			for (Item item : order.getLineItems()) {

				FrequentMeals frequestMeals = new FrequentMeals(order.getRestaurantId(), item.getId());

				FrequentMeals foundMeal = mapTotals.get(frequestMeals);
				if (foundMeal == null) {
					mapTotals.put(frequestMeals, frequestMeals);
					foundMeal = frequestMeals;
				}
				foundMeal.setCount(foundMeal.getCount().intValue() + item.getQuantity().intValue());

			}
		}

		List<FrequentMeals> list = new ArrayList<FrequentMeals>(mapTotals.values());

		Collections.sort(list, new Comparator<FrequentMeals>() {

			@Override
			public int compare(FrequentMeals f1, FrequentMeals f2) {
				return (f1.getCount().compareTo(f2.getCount()) * -1);
			}

		});

		List<FrequentMeals> finalList;
		if (list.size() < limit) {
			finalList = list;
		} else {
			finalList = list.subList(0, limit);
		}

		for (FrequentMeals frequentMeals : finalList) {

			List<Menu> listMenu = ofy.load().type(Menu.class).filter("restaurantId", frequentMeals.getRestaurantId()).list();

			for (Menu menu : listMenu) {
				for (MenuItem menuItem : menu.getMenuItems()) {

					if (frequentMeals.getId() != null) {

						if (menuItem.getId().longValue() == frequentMeals.getId().longValue()) {
							frequentMeals.setName(menuItem.getName());
						}
					}
				}
			}
		}

		return finalList;
	}

	/**
	 * returns a list of most frequent menu categories
	 * 
	 * @param limit
	 * @return
	 */
	private static List<FrequentCategory> calculateFrequentCategory() {
		final Objectify ofy = ObjectifyService.ofy();

		ObjectifyService.register(Restaurant.class);
		ObjectifyService.register(Order.class);
		ObjectifyService.register(Menu.class);

		List<FrequentCategory> list = new ArrayList<FrequentCategory>();

		// retrieve list of all restaurants
		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();

		// loop through all restaurants
		for (Restaurant restaurant : listRestaurant) {
			Map<CategoryCounter, CategoryCounter> mapTotals = new HashMap<CategoryCounter, CategoryCounter>();

			// get all orders for restaurant
			List<Order> listOrder = ofy.load().type(Order.class).filter("restaurantId", restaurant.getId()).list();

			// add up all items from each order
			for (Order order : listOrder) {
				for (Item item : order.getLineItems()) {

					CategoryCounter categoryCounter = new CategoryCounter(order.getRestaurantId(), item.getId());

					CategoryCounter foundMeal = mapTotals.get(categoryCounter);
					if (foundMeal == null) {
						mapTotals.put(categoryCounter, categoryCounter);
						foundMeal = categoryCounter;
					}
					foundMeal.setCount(foundMeal.getCount().intValue() + item.getQuantity().intValue());

				}
			}

			// get all menu items
			List<Menu> listMenu = ofy.load().type(Menu.class).filter("restaurantId", restaurant.getId()).list();

			Map<String, AtomicInteger> mapCategories = new HashMap<String, AtomicInteger>();

			for (Menu menu : listMenu) {
				for (MenuItem menuItem : menu.getMenuItems()) {

					for (CategoryCounter categoryCounter : mapTotals.values()) {

						if (categoryCounter.getId() != null) {
							if (menuItem.getId().longValue() == categoryCounter.getId().longValue()) {
								AtomicInteger count = mapCategories.get(menu.getCategory());
								if (count == null) {
									count = new AtomicInteger(0);
									mapCategories.put(menu.getCategory(), count);
								}
								count.getAndAdd(categoryCounter.getCount());
							}
						}
					}
				}
			}

			Set<String> setKeys = mapCategories.keySet();
			for (String key : setKeys) {
				AtomicInteger count = mapCategories.get(key);
				list.add(new FrequentCategory(restaurant.getId(), key, count.get()));
			}

		}

		Collections.sort(list, new Comparator<FrequentCategory>() {

			@Override
			public int compare(FrequentCategory f1, FrequentCategory f2) {
				if (f1.getRestaurantId().compareTo(f2.getRestaurantId()) != 0) {
					return f1.getRestaurantId().compareTo(f2.getRestaurantId());
				}
				return (f1.getCount().compareTo(f2.getCount()) * -1);
			}

		});

		return list;
	}

}
