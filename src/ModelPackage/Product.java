package ModelPackage;

import java.io.Serializable;

public class Product implements Serializable{
	//Attributes
	private String ID;
	private String Description;
	private double Price;
	private int InvCant;
	
	//Constructor
	public Product(String iD, String description, double price) {
		super();
		ID = iD;
		Description = description;
		Price = price;
		InvCant=10;
	}
	
	//getters and setters
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}

	public int getInvCant() {
		return InvCant;
	}

	public void setInvCant(int invCant) {
		InvCant = invCant;
	}
	
}
