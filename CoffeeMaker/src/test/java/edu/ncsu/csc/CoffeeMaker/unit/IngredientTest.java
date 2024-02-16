package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    @Autowired
    private IngredientService ingredientService;
    
    private Ingredient i; 
    private static final String COFFEE_NAME = "coffee";

    @BeforeEach
    public void setup () {
    	ingredientService.deleteAll();
    	i = new Ingredient(COFFEE_NAME, 2); 
    }
    
    @Test
    @Transactional 
    public void testIngredient() {
    	Assertions.assertNotNull(i);
    }
    
    @Test
    @Transactional
    public void testGetters() {
    	Assertions.assertEquals(COFFEE_NAME, i.getIngredient());
    	Assertions.assertEquals(2, i.getAmount());
    }
    
    @Test
    @Transactional
    public void testSetters() {
    	int newAmt = 4;
    	i.setAmount(newAmt);
    	Assertions.assertEquals(newAmt, i.getAmount());
    	
    	String chocName = "chocolate"; 
    	
    	i.setIngredient(chocName);
    	Assertions.assertEquals(chocName, i.getIngredient());
    }
    
    @Test
    @Transactional 
    public void testEquals() {
    	Assertions.assertTrue(i.equals(i));
    	
    	Ingredient eq = new Ingredient(COFFEE_NAME, 2); 
    	Assertions.assertTrue(i.equals(eq));
    	
    	eq.setAmount(3);
    	Assertions.assertFalse(i.equals(eq));
    	
    	String chocName = "chocolate"; 
    	
    	eq.setIngredient(chocName);
    	Assertions.assertFalse(i.equals(eq));
    	
    	eq.setAmount(2);
    	Assertions.assertFalse(i.equals(eq));
    	
    	// other cases that are false
    	Assertions.assertFalse(i.equals(null));
    	Assertions.assertFalse(i.equals(new Recipe()));
    }
    
    @Test
    @Transactional
    public void testHashCode() {
    	Assertions.assertEquals(i.hashCode(), i.hashCode());
    	
    	Ingredient eq = new Ingredient(COFFEE_NAME, 2); 
    	Assertions.assertEquals(eq.hashCode(), i.hashCode());
    	
    	eq.setAmount(4);
    	Assertions.assertNotEquals(eq.hashCode(), i.hashCode());
    }
    
    @Test
    @Transactional 
    public void testDeleteAll() {
    	
    }
    
    @Test
    @Transactional 
    public void testToString() {
    	
    }
    
}