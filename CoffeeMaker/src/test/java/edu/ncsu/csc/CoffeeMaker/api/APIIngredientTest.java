package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {
	
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	@Autowired
	private MockMvc               mvc;

	@Autowired
	private WebApplicationContext context;

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
	/**
	 * Tests the GET API Endpoint for all ingredients
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testGetIngredientsAPI() throws UnsupportedEncodingException, Exception {
		final Ingredient vanilla = new Ingredient();
		vanilla.setAmount( 3 );
		vanilla.setIngredient( "Vanilla" );
		
		final Ingredient pumpkinSpice = new Ingredient();
		pumpkinSpice.setAmount( 2 );
		pumpkinSpice.setIngredient( "Pumpkin Spice" );
		
		final Ingredient milk = new Ingredient();
		milk.setAmount( 10 );
		milk.setIngredient( "Milk" );
		
		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( vanilla ) ) ).andExpect( status().isOk() );
		
		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
	            .content( TestUtils.asJsonString( pumpkinSpice ) ) ).andExpect( status().isOk() );
		
		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
	            .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );
		
		String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		assertTrue(ingredients.contains("Vanilla"),
				"The GET API Endpoint should have retrieved a VANILLA ingredient, but did not.");
		assertTrue(ingredients.contains("Pumpkin Spice"),
				"The GET API Endpoint should have retrieved a PUMPKIN_SPICE ingredient, but did not.");
		assertTrue(ingredients.contains("Milk"),
				"The GET API Endpoint should have retrieved a MILK ingredient, but did not.");
	}
	
	/**
	 * Tests the GET API Endpoint for a single ingredient
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
//	@Test
//	@Transactional
//	public void testGetIngredientAPI() throws UnsupportedEncodingException, Exception {
//		final Ingredient coffee = new Ingredient();
//		coffee.setAmount( 10 );
//		coffee.setIngredient( "Coffee" );
//		
//		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
//	            .content( TestUtils.asJsonString( coffee ) ) ).andExpect( status().isOk() );
//		
//		String ingredient = mvc.perform( get( "/api/v1/ingredients/coffee" ) ).andDo( print() ).andExpect( status().isOk() )
//			    .andReturn().getResponse().getContentAsString();
//		
//		assertTrue(ingredient.contains("Coffee"),
//				"The GET API Endpoint should have retrieved a Coffee ingredient, but did not.");
//	}
	
	/**
	 * Test the POST API Endpoint for new ingredient
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testNewIngredientAPI() throws UnsupportedEncodingException, Exception {
		final Ingredient i = new Ingredient();
		i.setAmount( 3 );
		i.setIngredient( "Vanilla" );
		
		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
		 
		String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
				    .andReturn().getResponse().getContentAsString();
		 
		assertTrue(ingredients.contains("Vanilla"),
					"The POST API Endpoint should have created a Vanilla ingredient, but did not.");
		
		/* Attempt to add an ingredient that already exists in the inventory */
		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
	            .content( TestUtils.asJsonString( null ) ) ).andExpect( status().isBadRequest() );
	}
	
	/**
	 * Test the PUT API Endpoint for updating ingredients
	 * TODO Figure out what goes in TestUtils.asJsonString( __ ) 
	 * 		on line 170.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
//	@Test
//	@Transactional
//	public void testUpdateIngredientAPI() throws UnsupportedEncodingException, Exception {
//		final Ingredient i = new Ingredient();
//		i.setAmount( 3 );
//		i.setIngredient( "Vanilla" );
//		
//		mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
//		            .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
//		 
//		List<Ingredient> ingredient = new ArrayList<Ingredient>();
//		ingredient.add(new Ingredient("Vanilla", 5));
//		
//		Inventory addTo = new Inventory(ingredient);
//		
//		mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
//				.content( TestUtils.asJsonString( addTo ) ) ).andExpect( status().isOk() );
//		
//		String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
//			    .andReturn().getResponse().getContentAsString();
//		
//		assertTrue(ingredients.contains("5"),
//					"The POST API Endpoint should have updated the quantity of the Vanilla ingredient, but did not.");
//	}
	
	/**
	 * Tests the DELETE API Endpoint for an ingredient
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testDeleteIngredientAPI() throws UnsupportedEncodingException, Exception {
		final Ingredient i = new Ingredient();
		i.setAmount( 10 );
		i.setIngredient( "Coffee" );
		
		mvc.perform( delete( "/api/v1/ingredients/coffee" ).contentType( MediaType.APPLICATION_JSON )
	            .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
	 
		String ingredients = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
	 
		assertTrue(!ingredients.contains("Coffee"),
				"The DELETE API Endpoint should have removed a Coffee ingredient, but did not.");
	}
}
