package edu.ncsu.csc.CoffeeMaker;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {
	
	public static final String RECIPE_NAME = "Mocha";
 	public static final int PRICE = 350;
 	
	@Autowired
	private RecipeService recipeService;
	@Autowired
	private IngredientService ingredientService;
	
	private Ingredient coffee;
	private Ingredient milk;
	private Ingredient sugar;
	
	/**
	 * Sets up each test by deleting all records of type Recipe from the database
	 */
    @BeforeEach
    @Transactional
    public void setup () {
        recipeService.deleteAll();
        ingredientService.deleteAll();
        
        coffee = new Ingredient("coffee", 2);
        milk = new Ingredient("milk", 4);
        sugar = new Ingredient("sugar", 3);
    }
	
    /**
     * Testing Recipe functions such as adding, saving, and
     * editing a recipe (and verifying the edits), along with
     * finding all recipes in a list and finding a recipe by its name
     */
	@Test
	@Transactional
	public void testRecipes(){

		Recipe r = new Recipe();
		
		// set all fields of r 
		r.setName(RECIPE_NAME);
		r.addIngredient(milk);
		r.addIngredient(coffee);
		r.addIngredient(sugar);
		r.setPrice(PRICE);
		
		r = generateRecipe();

		recipeService.save(r);

		List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

		Assertions.assertEquals(1, dbRecipes.size());

		Recipe dbRecipe = dbRecipes.get(0);

		// test all fields of r with dbRecipe name 
		Assertions.assertEquals(r.getName(), dbRecipe.getName());
		Assertions.assertEquals(r.getIngredients(), dbRecipe.getIngredients());
		Assertions.assertEquals(r.getPrice(), dbRecipe.getPrice());
		Assertions.assertEquals(r.getClass(), dbRecipe.getClass());
		

		// create dbRecipe from RecipeService.findByName
		dbRecipe = recipeService.findByName(RECIPE_NAME);
		
		// test all fields of r with dpRecipe name 
		Assertions.assertEquals(r.getName(), dbRecipe.getName());
		Assertions.assertEquals(r.getIngredients(), dbRecipe.getIngredients());
		Assertions.assertEquals(r.getPrice(), dbRecipe.getPrice());
		Assertions.assertEquals(r.getClass(), dbRecipe.getClass());
		
		// edit recipe 
		dbRecipe.setPrice(400);
		
		// save recipe to database 
		recipeService.save(dbRecipe);
		
        Assertions.assertEquals(1, recipeService.count());

        Assertions.assertEquals(400, (int) ((Recipe) recipeService.findAll().get(0)).getPrice());
        
	}
	
	/**
	 * Tests the delete recipe method with two recipes in a list
	 */
	@Test
	@Transactional
	public void testDeleteRecipeWithMultipleRecipes() {
		
		// call helper method to create a recipe and saves it to the database
		Recipe r = generateRecipe();
		recipeService.save(r);
		
		// creates another recipe with another name and saves it to the database
		Recipe r1 = new Recipe();
		// set all fields of r 
		r1.setName("Caramel");
		milk.setAmount(5);
		r1.addIngredient(milk);
		r1.addIngredient(coffee);
		r1.addIngredient(sugar);
		r1.setPrice(PRICE);
				
		//recipeService.save(r1);
		
		List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
		
		Assertions.assertEquals(1, dbRecipes.size(),
				"Creating a recipe should result in one recipe in the database");
		
		// delete one of the recipes
		recipeService.delete(r);
		
		// checks that the deleted recipe does not exist
		Assertions.assertNull(recipeService.findByName(RECIPE_NAME),
				"Searching for a deleted recipe should return null");
		// checks that the count has been adjusted to only one recipe in the database
		Assertions.assertEquals(0, recipeService.count(),
				"Deleting one recipe should result in one recipe in the database");

		recipeService.save(r);
		r1 = new Recipe();
		// set all fields of r 
		r1.setName("Caramel");
		r1.addIngredient(new Ingredient("milk", 5));
		r1.addIngredient(new Ingredient("coffee", 2));
		r1.addIngredient(new Ingredient("sugar", 3));
		r1.setPrice(PRICE);
		recipeService.save(r1);
		
		dbRecipes = (List<Recipe>) recipeService.findAll();

		
		// checks that the size of the list is two after adding two recipes
		Assertions.assertEquals(2, dbRecipes.size(),
				"Creating two recipes should result in two recipes in the database");
		
		// delete one of the recipes
		recipeService.delete(r1);
		
		// checks that the deleted recipe does not exist
		Assertions.assertNull(recipeService.findByName("Caramel"),
				"Searching for a deleted recipe should return null");
		// checks that the count has been adjusted to only one recipe in the database
		Assertions.assertEquals(1, recipeService.count(),
				"Deleting one recipe should result in one recipe in the database");

	}
	
	/**
	 * Tests the delete recipe method with only one recipe in a list
	 */
	@Test
	@Transactional
	public void testDeleteOnlyRecipeInList() {
		
		// creates one recipe and saves it to the database
		Recipe r = generateRecipe();
		recipeService.save(r);
		
		// deletes the only recipe in the database
		recipeService.delete(r);
		// checks that the deleted recipe does not exist
		Assertions.assertNull(recipeService.findByName(RECIPE_NAME),
				"Searching for a deleted recipe should return null");
		// checks that the count has been adjusted to zero recipes in the database
		Assertions.assertEquals(0, recipeService.count(),
				"Deleting the only recipe in the database should result in zero recipes remaining");
		
	}
	
	/**
	 * Tests the delete recipe method when there are no recipes in a list
	 */
	@Test
	@Transactional
	public void testDeleteRecipeFromEmptyList() {
		
		// creates one recipe and saves it to the database
		Recipe r = generateRecipe();
		recipeService.save(r);
		
		// delete only recipe in database
		recipeService.delete(r);
		
		// checks that the count has been adjusted to zero recipes in the database
		Assertions.assertEquals(0, recipeService.count(),
				"Deleting the only recipe in the database should result in zero recipes remaining");
		// attempt to delete an already deleted recipe from database
		recipeService.delete(r);
		// checks that the deleted recipe does not exist because it was already deleted
		Assertions.assertNull(recipeService.findByName(RECIPE_NAME),
				"Searching for a deleted recipe in an empty list should return null");
	}

	/**
	 * Tests Ingredients functionality that an ingredient can be created an saved. 
	 */
	@Test 
	@Transactional
	public void testCreateIngredient() {
		coffee = new Ingredient("coffee", 3); 
		ingredientService.save(coffee);
		
		List<Ingredient> dbIngredients = ingredientService.findAll();
		Ingredient dbCoffee = dbIngredients.get(0);
		
		Assertions.assertEquals(1, dbIngredients.size(), 
				"Size of list of ingredeints should be 1");
		Assertions.assertEquals(coffee, dbCoffee, 
				"Ingredient saved to database should be equal to the ingedient retrieved from database");
	}
	
	/**
	 * Tests updating an ingredient 
	 */
	@Test
	@Transactional 
	public void testUpdateIngredient() { 
		coffee = new Ingredient("coffee", 3); 
		ingredientService.save(coffee);
		
		coffee.setAmount(6);
		ingredientService.save(coffee);
		
		List<Ingredient> dbIngredients = ingredientService.findAll();
		Ingredient dbCoffee = dbIngredients.get(0);
		
		// this is not working (size of list keeps growing, but should be reset each time the test is run)
		Assertions.assertEquals(1, dbIngredients.size(), 
				"Size of list of ingredeints should be 1");
		Assertions.assertEquals(coffee, dbCoffee, 
				"Ingredient saved to database should be equal to the ingedient retrieved from database");
		Assertions.assertEquals(coffee.getAmount(), dbCoffee.getAmount(), 
				"Ingredient saved to database should be have the same amount as the ingedient retrieved from database");

	}
	
	/**
	 * Tests deleting ingredients 
	 */
	@Test
	@Transactional
	public void testDeleteIngredient() {
		ingredientService.save(coffee);
		ingredientService.save(milk);
		ingredientService.save(sugar);
		
		Assertions.assertEquals(3, ingredientService.count(), "There should be 3 ingredients");
		
		ingredientService.delete(coffee);
		
		Assertions.assertEquals(2, ingredientService.count(), "There should be 2 ingredients");
	}
	
	/**
	 * A helper method to generate a new recipe with set arguments
	 * and saves it to the database
	 * 
	 * @return Recipe
	 * 				a newly created recipe
	 */
	private Recipe generateRecipe() {
		
		Recipe r = new Recipe();
		
		// set all fields of r 
		r.setName(RECIPE_NAME);
		r.addIngredient(milk);
		r.addIngredient(coffee);
		r.addIngredient(sugar);
		r.setPrice(PRICE);

		recipeService.save(r);
		
		return r;
	}
	
}
