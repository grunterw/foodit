package com.foodit.server.services.menu;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import com.foodit.annotations.Documentation;
import com.foodit.data.menu.Menu;

/**
 * All end points for Menu Rest API calls.
 * 
 * All Logic is immediately handled over to corresponding service
 * 
 * @author Grant
 * 
 */
@Path("/menu")
@Documentation(description = "Rest calls for all 'Menu' calls")
public class MenuResource {

	/**
	 * Retrieves a menu category by id
	 * 
	 * @param menuId
	 *            Current Id of the menu category
	 * @return Menu Returns retrieved Menu Category
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/{menuId}")
	@Documentation(description = "Retrieves a specific menu")
	public static Menu getMenuById(@PathParam("menuId") Long menuId) throws WebApplicationException {
		return MenuService.getMenuById(menuId);
	}

	/**
	 * Save a new menu category
	 * 
	 * @param menu
	 *            the menu to be saved
	 * @return Menu returns the updated menu
	 * @throws WebApplicationException
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@Path("/new")
	@Documentation(description = "Save a new Menu")
	public Menu createOrder(Menu menu) throws WebApplicationException {
		return MenuService.createMenu(menu);
	}

}