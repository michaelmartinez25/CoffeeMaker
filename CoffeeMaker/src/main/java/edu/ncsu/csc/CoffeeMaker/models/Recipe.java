package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Recipe price */
    @Min ( 0 )
    private Integer price;
    
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients; 

    
    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
    	this.ingredients = new ArrayList<Ingredient>();
        this.name = "";
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
    	for (Ingredient i : this.ingredients) {
    		if (i.getAmount() != 0) return false;
    	}
        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }
    
    /**
     * Setter for recipe name  
     * 
     * @param name String name being set 
     */
    public void setName(String name) {
    	this.name = name;
    }

    /**
     * Setter for recipe price 
     * 
     * @param price int price being set 
     */
    public void setPrice(int price) {
    	this.price = price;
    }
    
    /**
     * Getter for recipe price
     * 
     * @return int recipe price
     */
    public int getPrice() {
    	return this.price; 
    }
    
    /**
     * Getter for recipe name 
     * 
     * @return String recipe name 
     */
    public String getName(){
    	return this.name; 
    }
    
    /**
     * Adds an ingredient to recipe 
     * 
     * @param i Ingredient being added 
     */
    public void addIngredient(Ingredient i) { 
    	this.ingredients.add(i);
    }
    
    /**
     * Gets all ingredients in the recipe 
     * 
     * @return list of all ingredients in the recipe
     */
    public List<Ingredient> getIngredients() {
    	return this.ingredients;
    }

    /**
     * Finds and returns and ingredient  
     * 
     * @param ingredientName Ingredient being searched for 
     * @return Ingredient of the same name or null
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
	 * Updates a recipe
	 * 
	 * @param r
	 */
	public void updateRecipe(Recipe r) {
		this.name = r.getName();
		this.price = r.getPrice();
		this.ingredients = r.getIngredients();
	}

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
    	String r = this.name + " with ingredients \n"; 
    	for (Ingredient i: this.ingredients) {
    		r += i + "\n";
    	}
        return r;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}
