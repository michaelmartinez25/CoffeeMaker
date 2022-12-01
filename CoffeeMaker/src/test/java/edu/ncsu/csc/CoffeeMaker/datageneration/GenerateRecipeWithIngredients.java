package edu.ncsu.csc.CoffeeMaker.datageneration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {
    
    @Autowired
    private RecipeService recipeService;
        
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private IngredientService ingredientService;

    
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    	ingredientService.deleteAll();
    	inventoryService.deleteAll();
    }
    
    @Test
//  @Transactional
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );

        r1.setPrice( 50 );

        r1.addIngredient( new Ingredient( "coffee", 10 ) );
        r1.addIngredient( new Ingredient( "pumpkin spice", 3 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        
        recipeService.save( r1 );
        
        printRecipes();
    }
    
    private void printRecipes () {
        for ( DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}