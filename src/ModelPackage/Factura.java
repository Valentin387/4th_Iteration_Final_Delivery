package ModelPackage;

import java.io.Serializable;

public class Factura implements Serializable{

	private String ID;
	private String Description;
	private double Total;
	
	//Constructor
	public Factura(String iD, String description, double price) {
		super();
		ID = iD;
		Description = description;
		Total = price;
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
	public double getTotal() {
		return Total;
	}
	public void setPrice(double price) {
		Total = price;
	}
}
