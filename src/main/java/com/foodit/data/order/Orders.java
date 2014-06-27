package com.foodit.data.order;

import java.util.List;

/**
 * Pojo for storing a list of orders for a restaurant.
 * 
 * Note: This is not a DB entity - only used when loading json data
 * 
 * @author Grant
 * 
 */
public class Orders {

	private List<Order> orders;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "Orders [orders=" + orders + "]";
	}
	
	
	
}
