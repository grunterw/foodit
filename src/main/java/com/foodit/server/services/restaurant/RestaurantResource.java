package com.foodit.server.services.restaurant;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import com.foodit.annotations.Documentation;
import com.foodit.data.menu.Menu;
import com.foodit.data.order.Order;
import com.foodit.data.restaurant.Restaurant;

/**
 * All end points for Restaurant Rest API calls.
 * 
 * All Logic is immediately handled over to corresponding service
 * 
 * @author Grant
 * 
 */
@Path("/restaurant")
@Documentation(description = "Rest calls for all 'Restaurant' calls")
public class RestaurantResource {

	@GET
	@Produces("application/json")
	@Path("/all")
	@Documentation(description = "Retrieves all restaurants", returnClass = "com.foodit.data.restaurant.Restaurant")
	public static List<Restaurant> getAll() throws WebApplicationException {
		return RestaurantService.getAll();
	}

	@GET
	@Produces("application/json")
	@Path("/{restaurantId}")
	@Documentation(description = "Retrieves restaurant for specific id")
	public static Restaurant getById(@PathParam("restaurantId") Long restaurantId) throws WebApplicationException {
		return RestaurantService.getById(restaurantId);
	}

	@GET
	@Produces("application/json")
	@Path("/{restaurantId}/menu")
	@Documentation(description = "Retrieves for specific restaurant", returnClass = "com.foodit.data.menu.Menu")
	public static List<Menu> getMenuForRestaurant(@PathParam("restaurantId") Long restaurantId) throws WebApplicationException {
		return RestaurantService.getMenuForRestaurant(restaurantId);
	}

	@GET
	@Produces("application/json")
	@Path("/{restaurantId}/orders")
	@Documentation(description = "Retrieves for specific restaurant", returnClass = "com.foodit.data.order.Order")
	public static List<Order> getOrdersForRestaurant(@PathParam("restaurantId") Long restaurantId) throws WebApplicationException {
		return RestaurantService.getOrdersForRestaurant(restaurantId);
	}

}
