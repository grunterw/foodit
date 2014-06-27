package com.foodit.data.menu;

/**
 * Pojo for PricesForSize FIXME this should be a name, value object where name
 * is Regular,Quater etc. Also how is this different from SizeAndPrice ?
 * 
 * @author Grant
 * 
 */
public class PricesForSize {

	private Double regular;
	private Double quater;
	private Double half;
	private Double whole;
	private Double extrasauce;

	public Double getRegular() {
		return regular;
	}

	public void setRegular(Double regular) {
		this.regular = regular;
	}

	public Double getQuater() {
		return quater;
	}

	public void setQuater(Double quater) {
		this.quater = quater;
	}

	public Double getHalf() {
		return half;
	}

	public void setHalf(Double half) {
		this.half = half;
	}

	public Double getWhole() {
		return whole;
	}

	public void setWhole(Double whole) {
		this.whole = whole;
	}

	public Double getExtrasauce() {
		return extrasauce;
	}

	public void setExtrasauce(Double extrasauce) {
		this.extrasauce = extrasauce;
	}

}
