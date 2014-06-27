package com.foodit.data.menu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Pojo for MealTypeOptions
 * @author Grant
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealTypeOptions {

	private Long id;

	private String name;
	private boolean multiSelect;
	private boolean dropDown;

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

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public boolean isDropDown() {
		return dropDown;
	}

	public void setDropDown(boolean dropDown) {
		this.dropDown = dropDown;
	}

	private List<Options> options;

	public List<Options> getOptions() {
		return options;
	}

	public void setOptions(List<Options> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "MealTypeOptions [id=" + id + ", name=" + name + ", multiSelect=" + multiSelect + ", dropDown=" + dropDown + ", options="
				+ options + "]";
	}

}
