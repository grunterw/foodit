package com.foodit.data.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Pojo for an order Item
 * 
 * @author Grant
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

	private Long id;

	private String name;

	private Double price;

	private Double total;
	private Double unitPrice;
	private Integer quantity;
	private boolean promotion;
	private List<MealOption> mealOptions;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public boolean isPromotion() {
		return promotion;
	}

	public void setPromotion(boolean promotion) {
		this.promotion = promotion;
	}

	public List<MealOption> getMealOptions() {
		return mealOptions;
	}

	public void setMealOptions(List<MealOption> mealOptions) {
		this.mealOptions = mealOptions;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", total=" + total + ", unitPrice=" + unitPrice + ", quantity="
				+ quantity + ", promotion=" + promotion + ", mealOptions=" + mealOptions + "]";
	}

}
