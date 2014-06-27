package com.foodit.data.menu;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Pojo for Menu - contains category and then list of menu items
 * 
 * @author Grant
 * 
 */
@Entity
public class Menu {
	/**
	 * register entity with Objectify Service
	 */
	static {
		ObjectifyService.register(Menu.class);
	}

	@Id
	Long id;

	@Index
	Long restaurantId;

	private List<MenuItem> menuItems;

	private String category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	@Override
	public String toString() {
		return "Menu [id=" + id + ", restaurantId=" + restaurantId + ", menuItems=" + menuItems + ", category=" + category + "]";
	}

}
