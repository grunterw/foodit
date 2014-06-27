package com.foodit.data.order;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Pojo and Entity for Order
 * 
 * @author Grant
 * 
 */
@Entity
public class Order {

	/**
	 * register entity with Objectify Service
	 */
	static {
		ObjectifyService.register(Order.class);
	}

	@Id
	Long id;

	@Index
	Long restaurantId;

	private Integer recVersion;
	private Long orderId;
	private Integer easyOrderNum;
	private String created;
	private String storeId;
	private String storeName;
	private Double totalValue;
	private List<Item> lineItems;

	private String status;
	private String timeZoneId;
	private String collectionType;
	private String paymentType;
	private String addressDisplay;
	private String contactNumber;
	private String domain;
	private String currencyDisplay;

	private Integer commissionRate;
	private String referenceNumber;
	private String receiptLogoUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Integer getRecVersion() {
		return recVersion;
	}

	public void setRecVersion(Integer recVersion) {
		this.recVersion = recVersion;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getEasyOrderNum() {
		return easyOrderNum;
	}

	public void setEasyOrderNum(Integer easyOrderNum) {
		this.easyOrderNum = easyOrderNum;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public List<Item> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<Item> lineItems) {
		this.lineItems = lineItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getAddressDisplay() {
		return addressDisplay;
	}

	public void setAddressDisplay(String addressDisplay) {
		this.addressDisplay = addressDisplay;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCurrencyDisplay() {
		return currencyDisplay;
	}

	public void setCurrencyDisplay(String currencyDisplay) {
		this.currencyDisplay = currencyDisplay;
	}

	public Integer getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(Integer commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getReceiptLogoUrl() {
		return receiptLogoUrl;
	}

	public void setReceiptLogoUrl(String receiptLogoUrl) {
		this.receiptLogoUrl = receiptLogoUrl;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", restaurantId=" + restaurantId + ", recVersion=" + recVersion + ", orderId=" + orderId
				+ ", easyOrderNum=" + easyOrderNum + ", created=" + created + ", storeId=" + storeId + ", storeName=" + storeName
				+ ", totalValue=" + totalValue + ", lineItems=" + lineItems + ", status=" + status + ", timeZoneId=" + timeZoneId
				+ ", collectionType=" + collectionType + ", paymentType=" + paymentType + ", addressDisplay=" + addressDisplay
				+ ", contactNumber=" + contactNumber + ", domain=" + domain + ", currencyDisplay=" + currencyDisplay + ", commissionRate="
				+ commissionRate + ", referenceNumber=" + referenceNumber + ", receiptLogoUrl=" + receiptLogoUrl + "]";
	}

}
