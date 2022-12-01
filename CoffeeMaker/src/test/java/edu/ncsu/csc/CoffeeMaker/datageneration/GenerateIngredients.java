
package edu.ncsu.csc.CoffeeMaker.datageneration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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
public class GenerateIngredients {

    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @BeforeEach
    @Transactional
    public void setUp() {
    	recipeService.deleteAll();
    	ingredientService.deleteAll();
    	inventoryService.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateIngredients() {
    	    
        Ingredient i1 = new Ingredient("coffee", 5 );

        ingredientService.save( i1 );
        
        Assertions.assertEquals(1, ingredientService.findAll().size(), "Size of ingredient list should be 1");

        Assertions.assertEquals(1, ingredientService.count(), "There should be one ingredient in IngredientService");
        
        Ingredient i2 = new Ingredient("milk", 3 );

        ingredientService.save( i2 );
        
        Assertions.assertEquals(2, ingredientService.findAll().size(), "Size of ingredient list should be 2");
                
        Assertions.assertEquals( 2, ingredientService.count(), "There should be two ingredients in IngredientService");

        
    }
}
