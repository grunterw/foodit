package com.foodit.data.menu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

public class TestLoadMenus {

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void loadPojoFromFile() {

		try {
			URL jsonResource = Resources.getResource("Menus.json");
			File jsonFile = new File(jsonResource.toURI());

			Menus menus = mapper.readValue(jsonFile, Menus.class);

			assertNotNull("Menus is null", menus);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void test_LargeFile() {
		try {
			URL jsonResource = Resources.getResource("AllMenus.json");
			File jsonFile = new File(jsonResource.toURI());

			Menus allMenus = mapper.readValue(jsonFile, Menus.class);

			assertEquals("Number of categories", 7, allMenus.getMenu().get(0).getMenuItems().size());

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

}
