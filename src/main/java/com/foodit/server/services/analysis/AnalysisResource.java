package com.foodit.server.services.analysis;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

import com.foodit.annotations.Documentation;
import com.foodit.data.restaurant.AmountAnalysis;
import com.foodit.data.restaurant.FrequentCategory;
import com.foodit.data.restaurant.FrequentMeals;
import com.foodit.data.restaurant.OrderAnalysis;

/**
 * All end points for Analysis Rest API calls.
 * 
 * All Logic is immediately handled over to corresponding service
 * 
 * @author Grant
 * 
 */
@Path("/analysis")
@Documentation(description = "Rest calls for all 'Analysis' calls")
public class AnalysisResource {

	/**
	 * total number of orders by restaurant
	 * 
	 * @return
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/numberorders")
	@Documentation(description = "Retrieves the number of orders for each restaurants")
	public static List<OrderAnalysis> getNumberOrders() throws WebApplicationException {
		return AnalysisService.getNumberOrders();
	}

	/**
	 * total amount of orders by restaurant
	 * 
	 * @return
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/totalamount")
	@Documentation(description = "Retrieves the number of orders for each restaurants")
	public static List<AmountAnalysis> getTotalAmount() throws WebApplicationException {
		return AnalysisService.getTotalAmount();
	}

	/**
	 * Retrieve the most frequent meals
	 * 
	 * @return
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/frequentmeals")
	@Documentation(description = "Retrieves the most frequent meals")
	public static List<FrequentMeals> getFrequentMeals() throws WebApplicationException {
		return AnalysisService.getFrequentMeals();
	}

	/**
	 * Retrieve the most frequent meals - limited by count
	 * 
	 * @return
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/frequentmeals/{count}")
	@Documentation(description = "Retrieves the most frequent meals limited by count")
	public static List<FrequentMeals> getFrequentMeals(@PathParam("count") Integer count) throws WebApplicationException {
		return AnalysisService.getFrequentMeals(count);
	}

	/**
	 * Retrieve the most frequent category of meals
	 * 
	 * @return
	 * @throws WebApplicationException
	 */
	@GET
	@Produces("application/json")
	@Path("/frequentcategory")
	@Documentation(description = "Retrieves the most frequent category")
	public static List<FrequentCategory> getFrequentCategory() throws WebApplicationException {
		return AnalysisService.getFrequentCategory();
	}

}
