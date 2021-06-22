package ViewPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ControllerPackage.Check;
import ModelPackage.Factura;
import ModelPackage.FileDirection;
import ModelPackage.Product;

public class Bill {

	//Attributes
	//Tags
	private JLabel ID, Error; //Descrip
	//Fields
	private JTextField IDI, DescripI, UnitPriceI;
	//TextAreas F: for inventory
	private JTextArea F;
	//inventory: checking inventory
	private JPanel display;
	/*
	 * You are initializing the ObjectInputStream before the ObjectOutputStream. 
	 * So when the ObjectInputStream is created the file doesn't exist. 
	 * Fix that to match the order of initialization in the book and you'll 
	 * get a successful run...
	 * source: https://coderanch.com/t/659067/certification/EOFException-ObjectInputStream-ObjectOutputSteam
	 */
	private boolean alreadySomethingWritten, search;
	
	//Getters and Setters 
	public boolean isAlreadySomethingWritten() {
		return alreadySomethingWritten;
	}
	public void setAlreadySomethingWritten(boolean alreadySomethingWritten) {
		this.alreadySomethingWritten = alreadySomethingWritten;
	}

	//Constructor
	public Bill (JFrame app, JPanel menu, String type) {
		//it receives the JFrame object
				//and the previous JPanel object
		//I set the left JPanel
		display = new JPanel();
		display.setSize(510,400);
		display.setLayout(null);
		app.add(display);
		
		
		ID = new JLabel("ID Factura");
		ID.setBounds(10, 40, 100, 20);
		Error = new JLabel();
		Error.setBounds(10, 320, 150, 20);
		
		display.add(ID);
		display.add(Error);
		
		IDI = new JTextField();
		IDI.setBounds(80, 40, 120, 20);
				
		display.add(IDI);
		
		//I add the JTextArea
		F = new JTextArea(10, 20);
		F.setBounds(230, 10, 250, 340);
		F.setEditable(false);
		
		display.add(F);
		
		JButton  AV= new JButton("Buscar");
		AV.setBounds(10, 90, 160, 20);
		AV.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	  if(alreadySomethingWritten) {
		    		  try {
							boolean found = false;
							
							ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileDirection.billFile));
							ArrayList<Factura> facturas = new ArrayList<>(); 
							facturas = (ArrayList<Factura>) ois.readObject();
							
							for(Factura myBill : facturas) {
								if (myBill.getID().equals(IDI.getText())){
									Error.setText(null);
									F.setText(myBill.getDescription());
									found = true;
								}
							}
							
							if (found == false){
								Error.setText("Factura no encontrada");
							}
							search = true;
							ois.close();
						}catch(FileNotFoundException e1) {
							e1.printStackTrace();
						}catch(IOException e2) {
							e2.printStackTrace();
						}catch(ClassNotFoundException e3) {
							e3.printStackTrace();
						}
		    	  }
		    	  
		      }
		});
		display.add(AV); //I add this button to the JPanel
		
	
		//Button for making the Inventory panel invisible and menu panel visible again
		JButton Back = new JButton("Atrás");
		Back.setBounds(10, 140, 90, 20);
		Back.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent arg0) {
			    	if (search) {
			    		agregar();
			    		search = false;
			    	}else {
				    	display.setVisible(false);
					    menu.setVisible(true);
					    app.setTitle("PDV Nueva Era - Menu");
					    F.setText(null);
					    IDI.setText(null);
					    //DescripI.setText(null);
					    //UnitPriceI.setText(null);
			    	}
				}
			});
		display.add(Back); //I add the button to the JPanel
		
		//Button for deleting all content in the inventory
		JButton Delete = new JButton("Borrar todo");
		Delete.setBounds(100, 290, 110, 20);
		Delete.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent arg0) {
			    	//I use this receptor f1 to delete the file
				    File f3 = new File(FileDirection.billFile);
				    f3.delete();
				    //I create a new File under the same name
				    File f4 = new File(FileDirection.billFile);
					try {
						f4.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					//I said to the machine that I did this operation
				    setAlreadySomethingWritten(false);
				    //Design: I empty the JTextArea
				    F.setText(null);
				    agregar();
				}
			});
		if (type.equals("admin")) {
			display.add(Delete); //I add the button to the JPanel
		}
		
		display.setVisible(false);
		
	}
	
	
	
	//Method for setting features when is opened this page "inventory"
	public void agregar() {
		display.setVisible(true);
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
		F.setText(null);
		F.append(formattedDate +"\n" + "Nueva Era" +"\n\n" + "ID Factura\tTotal\n");
		
		//A little validation First
		File f3 = new File(FileDirection.billFile);
		if(f3.length() <=0) {
			//System.out.println("IT'S EMPTY");
			setAlreadySomethingWritten(false);
		}else {
			//System.out.println("IT'S NOT EMPTY");;
			setAlreadySomethingWritten(true);
		}
		
		//Read the DATABASE (.dat) for the bills
		if(this.alreadySomethingWritten==true) {
			try {
				
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileDirection.billFile));
				ArrayList<Factura> facturas = new ArrayList<>(); 
				facturas = (ArrayList<Factura>) ois.readObject();
				
				//System.out.println(facturas.isEmpty());
				
				for(Factura myBill : facturas) {
					F.append(myBill.getID() +"\t" + myBill.getTotal()+"\n");
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
			File f4 = new File(FileDirection.billFile);
			try {
				f4.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}



