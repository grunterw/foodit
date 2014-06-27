package com.foodit.data.menu;

import java.util.List;

/**
 * Pojo for menu items
 * @author Grant
 *
 */
public class MenuItem {

	Long id;

	private String name;
	private String description;
	private String category;

	private SizeAndPrice sizeAndPrice;

	private Double startingFromPrice;

	private List<MealTypeOptions> mealTypeOptions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public SizeAndPrice getSizeAndPrice() {
		return sizeAndPrice;
	}

	public void setSizeAndPrice(SizeAndPrice sizeAndPrice) {
		this.sizeAndPrice = sizeAndPrice;
	}

	public Double getStartingFromPrice() {
		return startingFromPrice;
	}

	public void setStartingFromPrice(Double startingFromPrice) {
		this.startingFromPrice = startingFromPrice;
	}

	public List<MealTypeOptions> getMealTypeOptions() {
		return mealTypeOptions;
	}

	public void setMealTypeOptions(List<MealTypeOptions> mealTypeOptions) {
		this.mealTypeOptions = mealTypeOptions;
	}

	@Override
	public String toString() {
		return "MenuItem [id=" + id + ", name=" + name + ", description=" + description + ", category=" + category + ", sizeAndPrice="
				+ sizeAndPrice + ", startingFromPrice=" + startingFromPrice + ", mealTypeOptions=" + mealTypeOptions + "]";
	}

}
