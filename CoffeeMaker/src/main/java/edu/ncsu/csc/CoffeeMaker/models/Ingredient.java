package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Ingredient model for the coffee maker. Ingredient is tied to the database using
 * Hibernate libraries. 
 * 
 * @author Sophie Lipset
 * @author Zeb Becker
 * @author Jo Caico
 * @author Michael Martinez 
 * @version November 29, 2022 
 */
@Entity
public class Ingredient extends DomainObject {
	
	/**
	 * Unique ID for ingredient
	 */
	@Id
	@GeneratedValue
	private Long id; 
	
	/**
	 * String name 
	 */
	private String ingredient; 
	
	/**
	 * Ingredient quantity 
	 */
	private int amount; 

	/**
	 * Specific ingredient constructor
	 * 
	 * @param ingredient
	 * @param amount
	 */
	public Ingredient(String ingredient, int amount) {
		super();
		this.amount = amount;
		this.ingredient = ingredient;
	}
	
	/**
	 * Empty constructor for Hibernate
	 */
	public Ingredient() { 

	}

	/**
	 * Getter for ingredient name
	 * 
	 * @return Ingredient name 
	 */
	public String getIngredient() {
		return ingredient;
	}

	/**
	 * Getter for ingredient amount 
	 * 
	 * @return Ingredient amount 
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Setter for ingredient name 
	 * 
	 * @param ingredient Ingredient name 
	 */
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	/**
	 * Setter for ingredient amount 
	 * 
	 * @param amount Ingredient amount 
	 */
	public void setAmount(int amount) {
		this.amount = amount;
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
	
	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		return amount == other.amount && ingredient == other.ingredient;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(amount, id, ingredient);
	}

	@Override
	public Serializable getId() {
		return id;
	}

}
