package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * RecipeRepository is used to provide CRUD operations for the Recipe model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Sophie Lipset
 * @author Zeb Becker
 * @author Jo Caico
 * @author Michael Martinez 
 * @version November 28, 2022
 */
public interface IngredientRepository extends JpaRepository <Ingredient, Long> {
	
}
