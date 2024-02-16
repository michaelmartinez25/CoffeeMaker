package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        
        List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
        ingredients.add(new Ingredient("Chocolate", 500)); 
        ingredients.add(new Ingredient("Coffee", 500)); 
        ingredients.add(new Ingredient("Milk", 500)); 
        ingredients.add(new Ingredient("Sugar", 500)); 
        ivt.addIngredients(ingredients); 
  
        inventoryService.save( ivt );
    }
    
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient(new Ingredient("Chocolate", 10));
        recipe.addIngredient(new Ingredient("Milk", 20));
        recipe.addIngredient(new Ingredient("Sugar", 5));
        recipe.addIngredient(new Ingredient("Coffee", 1));

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getIngredientAmount("Chocolate"));
        Assertions.assertEquals( 480, (int) i.getIngredientAmount("Milk"));
        Assertions.assertEquals( 495, (int) i.getIngredientAmount("Sugar"));
        Assertions.assertEquals( 499, (int) i.getIngredientAmount("Coffee"));
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
        ingredients.add(new Ingredient("Coffee", 5)); 
        ingredients.add(new Ingredient("Milk", 3)); 
        ingredients.add(new Ingredient("Sugar", 7)); 
        ingredients.add(new Ingredient("Chocolate", 2)); 
        ivt.addIngredients(ingredients); 

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getIngredientAmount("Coffee"),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getIngredientAmount("Milk"),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getIngredientAmount("Sugar"),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getIngredientAmount("Chocolate"),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
        	List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
            ingredients.add(new Ingredient("Coffee", -5)); 
            ingredients.add(new Ingredient("Milk", 3)); 
            ingredients.add(new Ingredient("Sugar", 7)); 
            ingredients.add(new Ingredient("Chocolate", 2)); 
            ivt.addIngredients(ingredients); 
            
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Coffee"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Milk"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Sugar"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Chocolate"),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        
        try {
        	List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
            ingredients.add(new Ingredient("Coffee", 5)); 
            ingredients.add(new Ingredient("Milk", -3)); 
            ingredients.add(new Ingredient("Sugar", 7)); 
            ingredients.add(new Ingredient("Chocolate", 2)); 
            ivt.addIngredients(ingredients); 
            
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Coffee"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Milk"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Sugar"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Chocolate"),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );
        }

    }

    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();
        
        try {
        	List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
            ingredients.add(new Ingredient("Coffee", 5)); 
            ingredients.add(new Ingredient("Milk", 3)); 
            ingredients.add(new Ingredient("Sugar", -7)); 
            ingredients.add(new Ingredient("Chocolate", 2)); 
            ivt.addIngredients(ingredients); 
            
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Coffee"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Milk"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Sugar"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Chocolate"),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );
        }

    }

    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();
        
        try {
        	List<Ingredient> ingredients = new ArrayList<Ingredient>(); 
            ingredients.add(new Ingredient("Coffee", 5)); 
            ingredients.add(new Ingredient("Milk", 3)); 
            ingredients.add(new Ingredient("Sugar", 7)); 
            ingredients.add(new Ingredient("Chocolate", -2)); 
            ivt.addIngredients(ingredients); 
            
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Coffee"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Milk"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Sugar"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getIngredientAmount("Chocolate"),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );
        }

    }
    

    /**
     * Test validateAmount, ensuring that it properly validates only positive integers
     */
    @Test
    @Transactional
    public void testValidateAmount() {
    	final Inventory ivt = inventoryService.getInventory(); 

    	assertEquals(10, ivt.validateAmount("10")); 

    	try {
    		ivt.validateAmount("-9"); 
    	}
    	catch (IllegalArgumentException iae) {
    		//Exception expected, carry on. 
    	}

    	try {
    		ivt.validateAmount("Not a number"); 
    	}
    	catch (IllegalArgumentException iae){
    		//Exception expected, carry on. 
    	}

    }
    
    /**
     * Test existById in abstract class Service
     */
    @Test
    @Transactional
    public void testExistsById() {
    	boolean b = inventoryService.existsById((long) 1); 
    	Assertions.assertFalse(b);
    }

    /**
     * Test findById in abstract class Service
     */
    @Test
    @Transactional
    public void testFindById() {
    	Assertions.assertNull(inventoryService.findById(null));
    	Assertions.assertNull(inventoryService.findById((long) 1));
    }
    
    /**
     * Test toString in class Inventory 
     */
    @Test
    @Transactional
    public void testToString() {
    	String str = ""; 
    	Inventory inv = inventoryService.getInventory(); 
    	for (Ingredient i : inv.getIngredients()) { 
    		str += i.getIngredient() + ": " + i.getAmount() + "\n";
    	}
    	Assertions.assertEquals(str, inv.toString());
    }

}
