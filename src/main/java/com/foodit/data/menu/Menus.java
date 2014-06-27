package com.foodit.data.menu;

import java.util.List;

/**
 * Pojo for storing a list of menu (categories) for a restaurant.
 * 
 * Note: This is not a DB entity - only used when loading json data
 * 
 * @author Grant
 * 
 */
public class Menus {

	private String restaurantId;

	private List<Menu> menu;

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}

}
