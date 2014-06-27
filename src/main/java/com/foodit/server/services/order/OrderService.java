package com.foodit.server.services.order;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodit.data.order.Order;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class OrderService {

	private final static Logger logger = LoggerFactory.getLogger(OrderService.class);

	public static Order getOrderById(Long orderId) throws WebApplicationException {

		Objectify ofy = ObjectifyService.ofy();
		Order order = ofy.load().type(Order.class).id(orderId).now();

		if (order == null) {
			logger.warn("Failed to find restaurant for Id: " + orderId);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		return order;
	}

	/**
	 * Creates an order
	 * 
	 * @param order
	 * @return
	 * @throws WebApplicationException
	 */
	// TODO - not implemented
	public static Order createOrder(Order order) throws WebApplicationException {
		throw new WebApplicationException(Response.Status.FORBIDDEN);
	}

}
