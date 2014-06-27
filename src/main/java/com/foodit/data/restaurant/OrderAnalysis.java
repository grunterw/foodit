package com.foodit.data.restaurant;

/**
 * Pojo container for Number of orders by restaurant
 * 
 * @author Grant
 * 
 */
public class OrderAnalysis {

	Long id;
	String name;
	Integer numberOrders;

	public OrderAnalysis(Long id, String name, Integer numberOrders) {
		super();
		this.id = id;
		this.name = name;
		this.numberOrders = numberOrders;
	}

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

	public Integer getNumberOrders() {
		return numberOrders;
	}

	public void setNumberOrders(Integer numberOrders) {
		this.numberOrders = numberOrders;
	}

	@Override
	public String toString() {
		return "OrderAnalysis [id=" + id + ", name=" + name + ", numberOrders=" + numberOrders + "]";
	}

}
