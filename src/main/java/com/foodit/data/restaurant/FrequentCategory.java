package com.foodit.data.restaurant;

public class FrequentCategory {

	private Long restaurantId;
	private String category;
	private Integer count;

	public FrequentCategory(Long restaurantId, String category, Integer count) {
		this.restaurantId = restaurantId;
		this.category = category;
		this.count = count;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
