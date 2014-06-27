package com.foodit.server.services.restaurant;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ext.Provider;

import org.junit.Test;
import org.mockito.Mockito;

import com.foodit.data.restaurant.Restaurant;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.container.servlet.WebComponent;
import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.inmemory.InMemoryTestContainerFactory;

public class TestRestaurantResource extends JerseyTest {
	// public TestRestaurantResource() throws Exception {
	// super("com.foodit.server.services.restaurant.RestaurantResource");
	// }

	public static RestaurantService mockRestaurantService = Mockito.mock(RestaurantService.class);

	@Override
	public WebAppDescriptor configure() {
		return new WebAppDescriptor.Builder()
				.initParam(WebComponent.RESOURCE_CONFIG_CLASS, ClassNamesResourceConfig.class.getName())
				.initParam(ClassNamesResourceConfig.PROPERTY_CLASSNAMES,
						RestaurantResource.class.getName() + ";" + MockRestaurantServiceProvider.class.getName() + ";"
				// + NotFoundMapper.class.getName()
				).build();
	}

	@Override
	public TestContainerFactory getTestContainerFactory() {
		return new InMemoryTestContainerFactory();
	}

	//@Test
	public void test_allRestaurants() {

		Restaurant restaurant = new Restaurant();
		List<Restaurant> listRestaurant = new ArrayList<Restaurant>();
		listRestaurant.add(restaurant);

		Mockito.when(RestaurantService.getAll()).thenReturn(listRestaurant);

		WebResource webResource = resource();
		String responseMsg = webResource.path("/restaurant/all").get(String.class);
		assertEquals("Hello World", responseMsg);
	}

	@Provider
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static class MockRestaurantServiceProvider extends SingletonTypeInjectableProvider {
		public MockRestaurantServiceProvider() {
			super(RestaurantService.class, mockRestaurantService);
		}
	}

	// @Path("hello")
	// public static class HelloResource {
	// @GET
	// public String getHello() {
	// return "Hello World!";
	// }
	// }
	//
	// @Override
	// protected Application configure() {
	// return new ResourceConfig(HelloResource.class);
	// }
	//
	// @Test
	// public void test() {
	// final String hello = target("hello").request().get(String.class);
	// assertEquals("Hello World!", hello);
	// }
}
