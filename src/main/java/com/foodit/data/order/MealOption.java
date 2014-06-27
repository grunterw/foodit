package com.foodit.data.order;

/**
 * POjo for Meal Options
 * 
 * @author Grant
 * 
 */
public class MealOption {
	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MealOption [name=" + name + ", value=" + value + "]";
	}

}
