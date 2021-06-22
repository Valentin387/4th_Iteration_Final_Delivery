package ViewPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ControllerPackage.Check;
import ModelPackage.FileDirection;
import ModelPackage.Product;

public class Inventory {

	//Attributes
	//Tags
	private JLabel ID, Descrip, UnitPrice, Error; //Descrip
	//Fields
	private JTextField IDI, DescripI, UnitPriceI;
	//TextAreas F: for inventory
	private JTextArea F;
	//inventory: checking inventory
	private JPanel inventory;
	/*
	 * You are initializing the ObjectInputStream before the ObjectOutputStream. 
	 * So when the ObjectInputStream is created the file doesn't exist. 
	 * Fix that to match the order of initialization in the book and you'll 
	 * get a successful run...
	 * source: https://coderanch.com/t/659067/certification/EOFException-ObjectInputStream-ObjectOutputSteam
	 */
	private boolean alreadySomethingWritten;
	
	//Getters and Setters 
	public boolean isAlreadySomethingWritten() {
		return alreadySomethingWritten;
	}
	public void setAlreadySomethingWritten(boolean alreadySomethingWritten) {
		this.alreadySomethingWritten = alreadySomethingWritten;
	}

	//Constructor
	public Inventory (JFrame app, JPanel menu, String type) {
		//it receives the JFrame object
				//and the previous JPanel object
		//I set the left JPanel
		inventory = new JPanel();
		inventory.setSize(575,400);
		inventory.setLayout(null);
		app.add(inventory);
		
		
		ID = new JLabel("ID");
		ID.setBounds(10, 40, 60, 20);
		Descrip = new JLabel("Descripción");
		Descrip.setBounds(10, 90, 60, 20);
		UnitPrice = new JLabel("Valor");
		UnitPrice.setBounds(10, 140, 60, 20);
		Error = new JLabel();
		Error.setBounds(10, 320, 150, 20);
		
		inventory.add(ID);
		inventory.add(Descrip);
		inventory.add(UnitPrice);
		inventory.add(Error);
		
		IDI = new JTextField();
		IDI.setBounds(80, 40, 120, 20);
		DescripI = new JTextField();
		DescripI.setBounds(80, 90, 120, 20);	
		UnitPriceI = new JTextField();
		UnitPriceI.setBounds(80, 140, 120, 20);
				
		inventory.add(IDI);
		inventory.add(DescripI);
		inventory.add(UnitPriceI);
		
		//I add the JTextArea
		F = new JTextArea(10, 20);
		F.setBounds(230, 10, 320, 340);
		F.setEditable(false);
		
		inventory.add(F);
		
		JButton  AV= new JButton("Agregar al Inventario");
		AV.setBounds(10, 200, 160, 20);
		AV.addActionListener(new ActionListener() {
		      @SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
		    	  
		    	  //I put the JText Field(s) content to the JTextArea and write into disc
		    	  if (IDI.getText().isEmpty() == false && DescripI.getText().isEmpty() == false && Check.decimal(UnitPriceI.getText())) {
	    			  
		    		  //I capture fields's information into a concrete object
		    		  Product p1 = new Product(IDI.getText(),DescripI.getText(),Double.parseDouble(UnitPriceI.getText()));	 
		    		  
		    		  try {
		    			  //I read the disc to extract pre-existent ArrayList into a temporal ArrayList
		    			    ArrayList<Product> tempInventory = new ArrayList<>(); 
		    			    if(alreadySomethingWritten==true) {
		    			    	ObjectInputStream preex = new ObjectInputStream(new FileInputStream(FileDirection.inventoryFile));
			    				tempInventory = (ArrayList<Product>) preex.readObject();
			    				preex.close(); //I close reader
			    				
		    			    }
		    				//I add the new article to the inventory
		    				tempInventory.add(p1);
		    				
		    				//I delete the old File represented by this temporal receptor f1
		    				File f1 = new File(FileDirection.inventoryFile);
		    				f1.delete();
		    				//I create a new file
		    				File f2 = new File(FileDirection.inventoryFile);
		
		    				//I write the updated ArrayList into the new file
		    			    
		    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f2));
		    				oos.writeObject(tempInventory);
		    				oos.close(); //I close writer
		    				if(alreadySomethingWritten==false) {
		    					alreadySomethingWritten=true;
		    				}
		    				
		    			}catch(FileNotFoundException e1) {
		    				e1.printStackTrace();
		    			}catch(IOException e2) {
		    				e2.printStackTrace();
		    			}catch(ClassNotFoundException e3) {
		    				e3.printStackTrace();
		    			}
		    		  
		    		  //Design:
		    		  Error.setText(null);
		    		  //I append the information in the big JTextArea
		    		  F.append(IDI.getText() +"\t" + DescripI.getText()+"\t" + UnitPriceI.getText()+ "\t" +p1.getInvCant() + "\n");
		    		  //I erase content in fields
		    		  IDI.setText(null);
			    	  DescripI.setText(null);
			    	  UnitPriceI.setText(null);
	    		  }else{
	    			  Error.setText("Valores no validos");
	    		  }
		    	  
		      }
		});
		inventory.add(AV); //I add this button to the JPanel
		
	
		//Button for making the Inventory panel invisible and menu panel visible again
		JButton Back = new JButton("Atrás");
		Back.setBounds(10, 250, 90, 20);
		Back.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent arg0) {
				    inventory.setVisible(false);
				    menu.setVisible(true);
				    app.setTitle("PDV Nueva Era - Menu");
				    F.setText(null);
				    IDI.setText(null);
				    DescripI.setText(null);
				    UnitPriceI.setText(null);
				    app.setSize(510,400);
				}
			});
		inventory.add(Back); //I add the button to the JPanel
		
		//Button for deleting all content in the inventory
		JButton Delete = new JButton("Borrar todo");
		Delete.setBounds(130, 290, 100, 20);
		Delete.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent arg0) {
			    	//I use this receptor f1 to delete the file
				    File f1 = new File(FileDirection.inventoryFile);
				    f1.delete();
				    //I create a new File under the same name
				    File f2 = new File(FileDirection.inventoryFile);
				    try {
						f2.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				    //I say to the machine that I did this operation
				    setAlreadySomethingWritten(false);
				    //Design: I empty the JTextArea
				    F.setText(null);
				}
			});
		if (type.equals("admin")) {
			inventory.add(Delete); //I add the button to the JPanel
		}
		
	
		JButton Restocking = new JButton("Reabastecer");
		Restocking.setBounds(10,290,110,20);
		Restocking.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					//I read the file to extract the object
    			    ArrayList<Product> currentInventory = new ArrayList<>(); 
    			    if(alreadySomethingWritten==true) {
    			    	ObjectInputStream preex = new ObjectInputStream(new FileInputStream(FileDirection.inventoryFile));
    			    	currentInventory = (ArrayList<Product>) preex.readObject();
	    				preex.close(); //I close reader
    			    }
					//I update every single object inside the ArrayList
					for(Product myProduct : currentInventory) {
						myProduct.setInvCant(10);
					}
					//I delete current file and I replace it with the updated one
    				File f1 = new File(FileDirection.inventoryFile);
    				f1.delete();
    				//I create a new file
    				File f2 = new File(FileDirection.inventoryFile);

    				//I write the updated ArrayList into the new file
    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f2));
    				oos.writeObject(currentInventory);
    				oos.close(); //I close writer
    				F.setText(null);
    				agregar();
					
				}catch(IOException e2) {
    				e2.printStackTrace();
    			}catch(ClassNotFoundException e3) {
    				e3.printStackTrace();
    			}
				
			}
		});
		
		inventory.add(Restocking);
		
		
		inventory.setVisible(false);
	}
	
	
	//Method for setting features when is opened this page "inventory"
	@SuppressWarnings("unchecked")
	public void agregar() {
		inventory.setVisible(true);
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
		F.setText(null);
		F.append(formattedDate +"\n" + "Nueva Era" +"\n\n" + "Producto\tDescripción\tValor\tStock\n");
		
		//Read the DATABASE (.dat) for the inventory
		if(this.alreadySomethingWritten==true) {
			try {
				
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileDirection.inventoryFile));
				ArrayList<Product> productInventory = new ArrayList<>(); 
				productInventory = (ArrayList<Product>) ois.readObject();
				
				for(Product myProduct : productInventory) {
					F.append(myProduct.getID() +"\t" + myProduct.getDescription()+"\t" + myProduct.getPrice()+"\t" +myProduct.getInvCant()+"\n");
				}
				
				ois.close();
			}catch(FileNotFoundException e1) {
				e1.printStackTrace();
			}catch(IOException e2) {
				e2.printStackTrace();
			}catch(ClassNotFoundException e3) {
				e3.printStackTrace();
			}
		}else {
			File f2 = new File(FileDirection.billFile);
			try {
				f2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}



