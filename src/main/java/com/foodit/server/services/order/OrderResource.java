package com.foodit.server.services.order;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import com.foodit.annotations.Documentation;
import com.foodit.data.order.Order;

/**
 * All end points for Restaurant Rest API calls.
 * 
 * All Logic is immediately handled over to corresponding service
 * 
 * @author Grant
 * 
 */
@Path("/order")
@Documentation(description = "Rest calls for all 'Restaurant' calls")
public class OrderResource {

	/**
	 * Retrieves an order by ID
	 * 
	 * @param orderId
	 *            The Id of the order
	 * @return Order the retrieved order
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/{orderId}")
	@Documentation(description = "Retrieves a specific order")
	public static Order getOrderById(@PathParam("orderId") Long orderId) throws WebApplicationException {
		return OrderService.getOrderById(orderId);
	}

	/**
	 * Saves a new order
	 * 
	 * @param order
	 *            the Order to be saved
	 * @return Order returns the updated order
	 * @throws WebApplicationException
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/new")
	@Documentation(description = "Save a new Order")
	public Order createOrder(Order order) throws WebApplicationException {
		return OrderService.createOrder(order);
	}

}