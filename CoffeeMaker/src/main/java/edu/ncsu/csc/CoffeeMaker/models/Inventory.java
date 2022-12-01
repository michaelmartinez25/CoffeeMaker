package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;
    /** List of ingredients in the inventory */
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Use this constructor to create inventory with an existing list of ingredient objects. 
     * 
     * @param ingredients : A list of ingredients to be added to the inventory
     */
    public Inventory (List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }
    
    /**
     * Get list of all ingredients in inventory
     */
    
    public List<Ingredient> getIngredients(){
    	return this.ingredients; 
    }
    
    /**
     * Returns the ingredient object with name ingredientName if one exists 
     * in the inventory, otherwise returns null.
     * @param ingredientName : name of ingredient we are searching for 
     * @return Ingredient object if one is found, else null
     */
    public Ingredient findIngredientByName(String ingredientName) { 
    	
    	
    	if (this.ingredients == null || this.ingredients.isEmpty()) {
    		return null; 
    	}
    	
    
    	for (Ingredient i : this.ingredients) {
    		if (i.getIngredient().equals(ingredientName)) return i;
    	}
    	return null;
    }
    
    /**
     * 
     * @param ingredientName : name of ingredient
     * @return quantity of named ingredient available in the inventory
     */
    public Integer getIngredientAmount(String ingredientName) {
    	Ingredient i = this.findIngredientByName(ingredientName);
    	if (i == null) return 0;
    	return i.getAmount();
    }
    
    /**
     * Sets the quantity of the named ingredient to amt. 
     * @param ingredientName : name of ingredient to set quantity of
     * @param amt : amount to set quantity of named ingredient to
     */
    public void setIngredientAmount(String ingredientName, Integer amt) {
    	if (amt < 0) return;
    	Ingredient i = this.findIngredientByName(ingredientName);
    	if (i == null) return;
    	i.setAmount(amt);
    }
    
    /**
     * Parses amount of an ingredient to be added from a string to an integer and validates that 
     * this quantity is a positive integer. 
     * @param stringAmt String representation of a quantity of ingredient to be added
     * @return parsed integer amount
     */
    public Integer validateAmount(String stringAmt) {
    	Integer amt = 0;
        try {
            amt = Integer.parseInt(stringAmt);
        }
        catch (final NumberFormatException e) {
            throw new IllegalArgumentException( "Units of an ingredient must be a positive integer" );
        }
        if (amt < 0) {
            throw new IllegalArgumentException( "Units of an ingredient must be a positive integer" );
        }

        return amt;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        for (Ingredient i : r.getIngredients()) {
        	// get existing ingredient of the same 
        	Ingredient inventoryIngredient = this.findIngredientByName(i.getIngredient());
        	if (inventoryIngredient == null) return false; 
        	if (inventoryIngredient.getAmount() < i.getAmount()) return false;
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
        	
        	for (Ingredient i : r.getIngredients()) {
        		int currentAmount = this.getIngredientAmount(i.getIngredient()); 
        		this.setIngredientAmount(i.getIngredient(), currentAmount - i.getAmount());
        		
        	}
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory. If an ingredient already exists in 
     * the inventory, the amount will be updated, not overwritten. 
     *
     * @param ingredients List of ingredients being added to the inventory 
     * @return true if successful, false if not
     */
    public boolean addIngredients (List<Ingredient> ingredients) {
    	
    	if (this.ingredients == null) {
    		this.ingredients = new ArrayList<Ingredient>();
    	}
    	
    	for (Ingredient i : ingredients) { 
    		// throw exception for negative amount
    		if (i.getAmount() < 0) throw new IllegalArgumentException( "Amount cannot be negative" );
    	}

    	// iterate through list of ingredients being added 
    	for (Ingredient i : ingredients) { 
    		Integer amt = i.getAmount();
    		String name = i.getIngredient();
    		Ingredient existingIngredient = this.findIngredientByName(name);
    		if (existingIngredient == null) {
    			this.ingredients.add(i);
    		} else {
    			this.setIngredientAmount(name, amt + existingIngredient.getAmount());
    		}
    	}
        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        
        for (Ingredient i : this.ingredients) {
        	buf.append(i.getIngredient() + ": "); 
        	buf.append(i.getAmount() + "\n"); 
        }

        return buf.toString();
    }

}
