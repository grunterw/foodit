package com.foodit.data.restaurant;

public class AmountAnalysis {

	Long id;
	String name;
	Double totalAmount;

	public AmountAnalysis(Long id, String name, Double totalAmount) {
		super();
		this.id = id;
		this.name = name;
		this.totalAmount = totalAmount;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "AmountAnalysis [id=" + id + ", name=" + name + ", totalAmount=" + totalAmount + "]";
	}

}
