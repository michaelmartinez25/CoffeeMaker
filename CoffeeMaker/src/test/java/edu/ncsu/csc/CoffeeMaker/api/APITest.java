package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
	
	/**
	 * Test API endpoints as specified by API Testing page of workshop
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	@Transactional
	public void testAPIEndpoints() throws UnsupportedEncodingException, Exception {
		
		// * First, make sure that there is a "Mocha" recipe
		String recipe = mvc.perform(get("/api/v1/recipes")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			
		//figure out if recipe we want is present 
		if (!recipe.contains("Mocha")) {
			//create new mocha recipe if needed
			final Recipe r = new Recipe();
			r.setChocolate(5);
			r.setCoffee(3);
			r.setMilk(2); 
			r.setSugar(1); 
			r.setPrice(10); 
			r.setName("Mocha"); 
				
			mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r))).andExpect(status().isOk()); 
		}
			
		// get recipes again- must now contain mocha because we just added it if it was not already there. 
		recipe = mvc.perform(get("/api/v1/recipes")).andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertTrue(recipe.contains("Mocha")); 
		
		// * Second, ensure that there are enough of each ingredient to make the recipe. 
		
		//create new inventory object with 50 of each ingredient
		final Inventory toAdd = new Inventory(50, 50, 50, 50); 
				
		//[put] is mapped to APIInventoryController.updateInventory(), 
		//adding the contents of toAdd to the active inventory. 
	
		mvc.perform(put("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(toAdd))).andExpect(status().isOk());
		
		// * Finally, we can test actually making the coffee
		mvc.perform(post("/api/v1/makecoffee/Mocha").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(100))).andExpect(status().isOk()); 
	}
	
	/**
	 * Additional API test to cover APIRecipeController.getRecipe()
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testGetRecipeByNameEndpoint() throws UnsupportedEncodingException, Exception {
		
		/* if there is no recipe with the input name, request status should be 404 error */
		String noRecipe = mvc.perform(get("/api/v1/recipes/NameNoRecipeHas")).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		//assertEquals(noRecipe, null); 
		
		/* First, make sure that there is a "Mocha" recipe */
		String recipe = mvc.perform(get("/api/v1/recipes")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
					
		//figure out if recipe we want is present 
		if (!recipe.contains("Mocha")) {
			//create new mocha recipe if needed
			final Recipe r = new Recipe();
			r.setChocolate(5);
			r.setCoffee(3);
			r.setMilk(2); 
			r.setSugar(1); 
			r.setPrice(10); 
			r.setName("Mocha"); 
						
			mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r))).andExpect(status().isOk()); 
		}
		
		/* Now that we know there is a Mocha recipe, the recipes/Mocha endpoint should return the recipe for a Mocha 
		 * with status OK */
		mvc.perform(get("/api/v1/recipes/Mocha")).andExpect(status().isOk());
					
	}
	
	@Test
	@Transactional
	public void testDeleteRecipe() throws UnsupportedEncodingException, Exception {
		String recipe = mvc.perform(get("/api/v1/recipes")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		if (!recipe.contains("Mocha")) {
			//create new mocha recipe if needed
			final Recipe r = new Recipe();
			r.setChocolate(5);
			r.setCoffee(3);
			r.setMilk(2); 
			r.setSugar(1); 
			r.setPrice(10); 
			r.setName("Mocha"); 
						
			mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r))).andExpect(status().isOk()); 
		}
		
		mvc.perform(delete("/api/v1/recipes/Mocha")).andExpect(status().isOk());
		/** Attempting to delete a recipe that doesn't exist should not work */
		mvc.perform(delete("/api/v1/recipes/Mocha")).andExpect(status().isNotFound());
	}
	
	
}
