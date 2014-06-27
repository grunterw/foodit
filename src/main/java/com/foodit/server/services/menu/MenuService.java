package com.foodit.server.services.menu;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodit.data.menu.Menu;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class MenuService {

	private final static Logger logger = LoggerFactory.getLogger(MenuService.class);

	/**
	 * Retrieves a menu by id
	 * 
	 * @param menuId
	 * @return
	 */
	public static Menu getMenuById(Long menuId) throws WebApplicationException {
		Objectify ofy = ObjectifyService.ofy();
		Menu menu = ofy.load().type(Menu.class).id(menuId).now();

		if (menu == null) {
			logger.warn("Failed to find restaurant for Id: " + menuId);
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		return menu;
	}

	/**
	 * Creates a new menu
	 * 
	 * @param menu
	 * @return Menu the new created menu object
	 */
	// TODO - not implemented

	public static Menu createMenu(Menu menu) throws WebApplicationException {
		throw new WebApplicationException(Response.Status.FORBIDDEN);
	}

}
