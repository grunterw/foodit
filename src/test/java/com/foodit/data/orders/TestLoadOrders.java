package com.foodit.data.orders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodit.data.order.Order;
import com.foodit.data.order.Orders;
import com.google.common.io.Resources;

public class TestLoadOrders {

	ObjectMapper mapper = new ObjectMapper();
	private Orders orders;

	@Before
	public void loadPojoFromFile() {
		try {
			// URL jsonResource =
			// TestLoadOrders.class.getResource("Orders.json");
			URL jsonResource = Resources.getResource("Orders.json");
			File jsonFile = new File(jsonResource.toURI());

			orders = mapper.readValue(jsonFile, Orders.class);
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void test_NumberOrders_Loaded() {
		try {

			assertEquals("Number of Orders", 2, orders.getOrders().size());

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void test_NumberLineItems_Loaded() {
		try {

			assertEquals("Number of LineItems", 2, orders.getOrders().get(0).getLineItems().size());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test_NumberMealOptions_Loaded() {
		try {

			assertEquals("Number of Meal Options", 2, orders.getOrders().get(1).getLineItems().get(0).getMealOptions().size());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void test_OrderDetails() {
		Order order = orders.getOrders().get(0);

		assertEquals("recVersion", new Integer(7), order.getRecVersion());
		assertEquals("orderId", new Long(5456722831343616l), order.getOrderId());
		assertEquals("easyOrderNum", new Integer(1), order.getEasyOrderNum());
		assertEquals("created", "2014-03-23T19:06:37.459Z", order.getCreated());
		assertEquals("storeId", "bbqgrill", order.getStoreId());
		assertEquals("storeName", "B-B-Q Grill", order.getStoreName());
		assertEquals("totalValue", new Double(9.50d), order.getTotalValue());
		assertEquals("status", "PaymentCaptured", order.getStatus());
		assertEquals("timeZoneId", "Europe/London", order.getTimeZoneId());
		assertEquals("collectionType", "Collection", order.getCollectionType());
		assertEquals("paymentType", "CREDIT_CARD", order.getPaymentType());
		assertEquals("addressDisplay", "1260 High Rd, London, N20 9HH", order.getAddressDisplay());
		assertEquals("contactNumber", "+442084467888", order.getContactNumber());
		assertEquals("domain", "www.bbqgrillwhetstone.co.uk", order.getDomain());
		assertEquals("currencyDisplay", "GBP", order.getCurrencyDisplay());
		assertEquals("commissionRate", new Integer(5), order.getCommissionRate());
		assertEquals("referenceNumber", "1", order.getReferenceNumber());
		assertEquals(
				"receiptLogoUrl",
				"http://foodit-prod.appspot.com/api/v1/serveLogo/AMIfv94d6PQjKmDQU27RTz8vDRqEzeTwgsXXDKpNxfxcVJSGSI_8JFq_XHTbHV_ggLv2GFLFGXaAhekq0B2J0S9Vq3emJuJnB6KgQE2rUvaawdxjng6V5sDPg6brOktvZAsW22q23vuolFCOwsu54kIsAHDI7OpUmw/",
				order.getReceiptLogoUrl());

	}

	@Test
	public void test_LargeFile() {
		try {
			URL jsonResource = Resources.getResource("AllOrders.json");
			File jsonFile = new File(jsonResource.toURI());

			Orders allOrders = mapper.readValue(jsonFile, Orders.class);

			assertEquals("Number of Orders", 86, allOrders.getOrders().size());

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
