package edu.ncsu.csc.CoffeeMaker.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Ingredients.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Sophie Lipset
 * @author Zeb Becker
 * @author Jo Caico
 * @author Michael Martinez 
 * @version November 18, 2022
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@RestController
public class APIIngredientController extends APIController {
	
    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
	@Autowired
	private IngredientService service; 
	
	/**
     * REST API method to provide GET access to all ingredients in the system
     *
     * @return JSON representation of all ingredients
     */
	@GetMapping ( BASE_PATH + "/ingredients")
	public List<Ingredient> getIngredients(){
		return service.findAll(); 
	}
	
    /**
     * REST API method to provide GET access to a specific ingredient, as indicated
     * by the path variable provided (the name of the ingredient desired)
     *
     * @param name Ingredient name 
     * @return response to the request
     */
//	@GetMapping ( BASE_PATH + "/ingredients/{name}") 
//	public ResponseEntity getIngredient( @PathVariable("name") final String name) {
//		
//		Ingredient i = service.findByName(name);
//		
//		if (i==null) {
//			return new ResponseEntity(errorResponse("No ingredient found with name " + name), HttpStatus.NOT_FOUND);  
//		}
//		
//		return new ResponseEntity(i, HttpStatus.OK); 
//	}
	
    /**
     * REST API method to provide POST access to the Ingredient model. This is used
     * to create a new Ingredient by automatically converting the JSON RequestBody
     * provided to a Ingredient object. Invalid JSON will fail.
     *
     * @param recipe The valid Ingredient to be saved.
     * @return ResponseEntity indicating success if the Recipe could be saved or an error if it could not be
     */
	@PostMapping (BASE_PATH + "/ingredients")
	public ResponseEntity createIngredient(@RequestBody final Ingredient ingredient) {
		//check for duplicate ingredient
//		if (null != service.findByName(ingredient.getIngredient().toString())) {
//			return new ResponseEntity(errorResponse("Ingredient with the name " + ingredient.getIngredient().toString() + " already exists."), HttpStatus.CONFLICT); 
//		}
		service.save(ingredient);
		return new ResponseEntity(successResponse(ingredient.getIngredient().toString() + " successfully created"), HttpStatus.OK);
	}
	
    /**
     * REST API method to allow deleting a Ingredient from the CoffeeMaker's
     * Inventory, by making a DELETE request to the API endpoint and indicating
     * the ingredient to delete (as a path variable)
     *
     * @param name The name of the Ingredient to delete
     * @return Success if the ingredient could be deleted; an error if the ingredient does not exist
     */
	@DeleteMapping (BASE_PATH + "/ingredients/{name}")
	public ResponseEntity deleteIngredient(@RequestBody final String name) {
//		Ingredient i = service.findByName(name); 
//		if (i == null) { 
//			return new ResponseEntity(errorResponse("No ingredient found with the name " + name), HttpStatus.NOT_FOUND);
//		} 
//		service.delete(i); 
		return new ResponseEntity(successResponse(name + " successfully deleted"), HttpStatus.OK);
	}
	
	
}