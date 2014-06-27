package com.foodit.server.services.restaurant;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodit.data.menu.Menu;
import com.foodit.data.order.Order;
import com.foodit.data.restaurant.Restaurant;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class RestaurantService {

	private final static Logger logger = LoggerFactory.getLogger(RestaurantService.class);

	/**
	 * Returns a list of all restaurants
	 * 
	 * @return Restaurant (only name and id)
	 */
	public static List<Restaurant> getAll() throws WebApplicationException {
		logger.debug("Request to retrive all restaurants");

		Objectify ofy = ObjectifyService.ofy();

		List<Restaurant> listRestaurant = ofy.load().type(Restaurant.class).list();

		return listRestaurant;
	}

	/**
	 * Returns a restaurant object for a specific id
	 * 
	 * @param restaurantId
	 * @return
	 */
	public static Restaurant getById(Long restaurantId) throws WebApplicationException {
		logger.debug("Request to retrive restaurant by Id");

		Objectify ofy = ObjectifyService.ofy();
		Restaurant restaurant = ofy.load().type(Restaurant.class).id(restaurantId).now();

		if (restaurant == null) {
			logger.warn("Failed to find restaurant for Id: " + restaurantId);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		return restaurant;
	}

	/**
	 * Retrieves all the menus for a specific restaurant
	 * 
	 * @param restaurantId
	 *            the Id of the restaurant
	 * @return A list of Menu Categories and meu items
	 */
	public static List<Menu> getMenuForRestaurant(Long restaurantId) throws WebApplicationException {
		logger.debug("Request to retrieve menu by restaurant Id");

		Objectify ofy = ObjectifyService.ofy();

		List<Menu> listMenu = ofy.load().type(Menu.class).filter("restaurantId", restaurantId).list();

		return listMenu;
	}

	/**
	 * Returns all the orders for a particular Restaurant
	 * 
	 * @param restaurantId
	 *            Id of the restaurant
	 * @return A list of orders
	 */
	public static List<Order> getOrdersForRestaurant(Long restaurantId) throws WebApplicationException {
		logger.debug("Request to retrieve orders by restaurant Id");

		Objectify ofy = ObjectifyService.ofy();

		List<Order> listOrder = ofy.load().type(Order.class).filter("restaurantId", restaurantId).list();

		return listOrder;
	}

}
