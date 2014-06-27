package com.foodit.data.restaurant;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Pojo and Entity for Restaurant
 * 
 * @author Grant
 * 
 */
@Entity
public class Restaurant {

	static {
		ObjectifyService.register(Restaurant.class);
	}

	@Id
	Long id;

	@Index
	String name;

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

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + "]";
	}

}
