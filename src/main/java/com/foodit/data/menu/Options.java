package com.foodit.data.menu;

/**
 * Pojo for options
 * @author Grant
 *
 */
public class Options {

	private String label;
	private PricesForSize pricesForSize;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public PricesForSize getPricesForSize() {
		return pricesForSize;
	}

	public void setPricesForSize(PricesForSize pricesForSize) {
		this.pricesForSize = pricesForSize;
	}

	@Override
	public String toString() {
		return "Options [label=" + label + ", pricesForSize=" + pricesForSize + "]";
	}

}
