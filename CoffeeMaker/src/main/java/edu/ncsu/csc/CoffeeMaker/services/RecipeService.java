package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.RecipeRepository;

/**
 * The RecipeService is used to handle CRUD operations on the Recipe model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Recipe by name.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class RecipeService extends Service<Recipe, Long> {

    /**
     * RecipeRepository, to be autowired in by Spring and provide CRUD
     * operations on Recipe model.
     */
    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    protected JpaRepository getRepository () {
        return recipeRepository;
    }
    
    @Override
    public void saveAll ( final List<Recipe> list ) {
    	for (Recipe r : list) {
    		if (r == null) return;
    		for (Ingredient i : r.getIngredients()) {
    			if (i.getAmount() < 0) return;
    		}
    	}
        getRepository().saveAll( list );
        getRepository().flush();
    }
    
    /**
     * Update a recipe
     * @param name
     * @param recipe
     */
    public void update(final String name, final Recipe recipe) {
    	Recipe r = recipeRepository.findByName( name );
    	getRepository().delete(r);
    	r = recipe;
		getRepository().save(r);
		getRepository().flush();
    }

    /**
     * Find a recipe with the provided name
     * 
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public Recipe findByName ( final String name ) {
        Recipe r = recipeRepository.findByName( name );
        if (r == null) {
        	return null;
        }
        
        if (r.getIngredients().isEmpty()) return null;
        for (Ingredient i : r.getIngredients()) {
        	if (i.getAmount() < 0) return null;
        }
        return r;
    }

}
