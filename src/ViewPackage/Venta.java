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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ControllerPackage.Check;
import ControllerPackage.IDCreator;

import ModelPackage.FileDirection;
import ModelPackage.Product;
import ModelPackage.Factura;

public class Venta{
	//Attributes
	//Tags
	private JLabel ID, C, Error, PF, Error2;
	//Fields
	private JTextField IDI, CI, PIF;
	//TextAreas F: for ventas, F2: for factura
	private JTextArea F, F2;
	//Total without taxes
	private double total;
	//venta: adding products, (Next Page)->factura: displaying final bill before paying
	private JPanel venta, factura;
	
	//This will help the buttons "Menu" and "atrás"
	private boolean TheSaleWasPayed=false;
	private ArrayList<Product> previousState=new ArrayList<>(); ;
	
	//A very important validation 
	private boolean alreadySomethingWrittenInv;
	private boolean alreadySomethingWrittenFac;
	
	//private boolean alreadySomethingWrittenInv;
	
	//Getters and Setters 
	public boolean isAlreadySomethingWrittenFac() {
		return alreadySomethingWrittenFac;
	}
	public void setAlreadySomethingWrittenFac(boolean alreadySomethingWrittenFac) {
		this.alreadySomethingWrittenFac = alreadySomethingWrittenFac;
	}
	
	//Getters and Setters 
	public boolean isAlreadySomethingWrittenInv() {
		return alreadySomethingWrittenInv;
	}
	public void setAlreadySomethingWrittenInv(boolean alreadySomethingWritten) {
		this.alreadySomethingWrittenInv = alreadySomethingWritten;
	}

	//Constructor
	public Venta(JFrame app, JPanel menu) {
		//it receives the JFrame object
		//and the previous JPanel object
		
			//Builds all what's inside the first JPanel (ventas)
		//I set the left JPanel
		venta = new JPanel();
		venta.setSize(510,400);
		venta.setLayout(null);
		app.add(venta);
		
		
		ID = new JLabel("ID");
		ID.setBounds(10, 40, 60, 20);
		C = new JLabel("Cantidad");
		C.setBounds(10, 90, 60, 20);
		Error = new JLabel();
		Error.setBounds(10, 300, 150, 20);
		
		venta.add(ID);
		venta.add(C);
		venta.add(Error);
		
		IDI = new JTextField();
		IDI.setBounds(80, 40, 120, 20);
		CI = new JTextField();
		CI.setBounds(80, 90, 120, 20);	
		
		venta.add(IDI);
		venta.add(CI);
		
		//I add the JTextArea
		F = new JTextArea(10, 20);
		F.setBounds(230, 10, 250, 340);
		F.setEditable(false);
		
		venta.add(F);
		
		
		JButton  AV= new JButton("Agregar");
		AV.setBounds(10, 200, 130, 20);
		AV.addActionListener(new ActionListener() {
		      @SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
		    	  //Put the JText Field(s) content to the JTextArea
		    	  //Remember:
		    	  //IDI = iD content
		    	  //CI  = quantity content
		    	  //UI  = price content
		    	  if (IDI.getText().isEmpty() == false && CI.getText().isEmpty() == false && Check.integer(CI.getText())) {
	    			  //I re-calculate the total
	    			  int quantity = Integer.parseInt(CI.getText());
		    		  Error.setText(null);
		    		  
		    		  //Process to get the description, price and available-quantity of X article in the store's inventory
		    		  String ExtractedDescription="";
		    		  double ExtractedUnitPrice=0;
		    		  int ExtractedInvCant=0;
		    		  boolean ArticleFound=false;
		    		  boolean ItIsEnough=false;
		    		  
		    		  try {
		    			ArrayList<Product> tempInventory = new ArrayList<>(); 
		    			//I open a reader to get my object form file
						ObjectInputStream inventOIS = new ObjectInputStream(new FileInputStream(FileDirection.inventoryFile));
						tempInventory = (ArrayList<Product>) inventOIS.readObject();
						inventOIS.close(); //I close reader
						
						for(Product myProduct : tempInventory) {
							//I'm looking for some match
							if(myProduct.getID().equalsIgnoreCase(IDI.getText())) {
								ExtractedDescription=myProduct.getDescription();
								ExtractedUnitPrice= myProduct.getPrice();
								ExtractedInvCant=myProduct.getInvCant();
								ArticleFound=true;
								break;
							}
						}
						//I verify if I have enough of X product to satisfy the client's demand
						if(quantity <= ExtractedInvCant) {
							ItIsEnough=true;
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
		    		  //Messages for the cashier
		    		  if(!ArticleFound || !ItIsEnough) {
		    			  if(!ArticleFound) {
		    				  JFrame frame = new JFrame("Article Not Found Frame");
			    			  JOptionPane.showMessageDialog(frame ,
			    					  "El ID ingresado no corresponde a ningún artículo","NotFound",
			    					  JOptionPane.INFORMATION_MESSAGE);
		    			  }else {
		    				  JFrame frame = new JFrame("Article Not Enough Frame");
			    			  JOptionPane.showMessageDialog(frame ,
			    					  "No hay suficiente de este artículo en el inventario","NotEnough",
			    					  JOptionPane.INFORMATION_MESSAGE);
		    			  }
		    			  
		    		  }else {
		    			  //Process of paying and updating inventory 
							try {
								
								ArrayList<Product> currentInventory = new ArrayList<>(); 
				    			//I open the reader to get my object from file
								ObjectInputStream inventOIS = new ObjectInputStream(new FileInputStream(FileDirection.inventoryFile));
								currentInventory  = (ArrayList<Product>) inventOIS.readObject();
								inventOIS.close(); //I close reader
								
								ArrayList<Product> tempInventory = new ArrayList<>();
								for(Product myProduct : currentInventory) {
									
									//I update available quantity of X product
									if(myProduct.getID().equalsIgnoreCase(IDI.getText())) {
										myProduct.setInvCant(myProduct.getInvCant() - quantity);
									}
									tempInventory.add(myProduct);
								}
								//Now I need to delete current inventory and replace it with tempInventory
								//I use this receptor f1 to delete the existing file
							    	File f1 = new File(FileDirection.inventoryFile);
							    	f1.delete();
							    //I create a new File under the same name
							    File f2 = new File(FileDirection.inventoryFile);
							    try {
									f2.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
								}
							    
							  //I write the updated ArrayList into the new file
			    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f2));
			    				oos.writeObject(tempInventory);
			    				oos.close(); //I close writer
									
							} catch (ClassNotFoundException | IOException e) {
								e.printStackTrace();
							}
							
		    			  
		    			  //I append the information in the big JTextArea			  
			    		  F.append(ExtractedDescription +"\t" + CI.getText()+"\t" + ExtractedUnitPrice+"\n");
			    		  total = total + (ExtractedUnitPrice * quantity); 
		    		  }
		    		
		    		  //I erase content in fields
		    		  IDI.setText(null);
			    	  CI.setText(null);
	    		  }else{
	    			  Error.setText("Valores no validos");
	    		  }
		    	  
		      }
		});
		venta.add(AV); //I add this button to the JPanel
		
		//Button for making the "Venta" panel invisible and menu panel visible again
		JButton Back = new JButton("Atrás");
		Back.setBounds(10, 250, 90, 20);
		Back.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	  //If the client or cashier regretted something, no problem,
		    	  //Because in previousState I have a backup of the inventory before
		    	  //the sale
		    	  if(!TheSaleWasPayed) {
		    		  try {
			    		  
				    		//Now I need to delete current inventory and replace it with the backup
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
							    
							  //I write the backup ArrayList into the new file
			    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f2));
			    				oos.writeObject(previousState);
			    				oos.close(); //I close writer
				    		  
				    	  }catch (IOException e) {
								e.printStackTrace();
						  }  
		    	  } 
		    	  
		    	  //
		    	  venta.setVisible(false);
		    	  menu.setVisible(true);
		    	  app.setTitle("PDV Nueva Era - Menu");
		    	  F.setText(null);
		    	  IDI.setText(null);
		    	  CI.setText(null);
		    	  total = 0;
		      }
		});
		venta.add(Back); //I add the button to the JPanel
		
		
		//Button for closing this page for adding products and going to the
		//page where I display the official BILL to the cashier
		
		JButton Fin = new JButton("Completar");
		Fin.setBounds(110, 250, 110, 20);
		Fin.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  venta.setVisible(false); //I make this invisible 
		    	  factura.setVisible(true); //I make the next page visible
		    	  F2.setText(F.getText()); //I set a new JTextArea with the current content
		    	  F2.append("\nTotal:\t" + total);
		    	  //I re-set the accumulative 
		    	  
		      }
		});
		venta.add(Fin); //I add this button to panel
		
		venta.setVisible(false); //I make this panel visible when it's first called
		
		
		
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////
		
			//Builds all what's inside the second JPanel (factura)
		factura = new JPanel();
		factura.setSize(510,400);
		factura.setLayout(null);
		app.add(factura);
				
		F2 = new JTextArea(10, 20);
		F2.setBounds(230, 10, 250, 340);
		F2.setEditable(false);
		
		PF = new JLabel("Pagado");
		PF.setBounds(10, 140, 100, 20);
		Error2 = new JLabel();
		Error2.setBounds(10, 300, 150, 20);
		
		factura.add(PF);
		factura.add(Error2);
		
		PIF = new JTextField();
		PIF.setBounds(80, 140, 120, 20);
		
		factura.add(PIF);
		
		
		JButton Fin2 = new JButton("Pagar");
		Fin2.setBounds(10, 200, 130, 20);
		Fin2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		    	  //Put the JText Field(s) content to the JTextArea
		    	  //Remember:
		    	  //IDI = iD content
		    	  //CI  = quantity content
				
				//The sale was successful 
				TheSaleWasPayed=true;
				//Now I add this BILL to the file with all the BILLS ever made in the store
				
		    	  if(PIF.getText().isEmpty() == false && Check.decimal(PIF.getText())) {
	    			  //I re-calculate the total
	    			  double pagado = Double.parseDouble(PIF.getText());
		    		  Error2.setText(null);
		    		  
		    		  if (pagado >= total) {
		    			  F2.append("\nPagado:\t" + pagado);
		    			  F2.append("\nDevuelta:\t" + (pagado - total));
		    			  PF.setText("Pago realizado");
				    	  
				    	  PIF.setVisible(false);
				    	  Fin2.setVisible(false);
				    	  
				    	  int n = 1;
				    	  
				    	  try {
			    			  //I read the disc to extract pre-existent ArrayList into a temporal ArrayList
			    			  //A little validation first
			    			  File f1 = new File(FileDirection.billFile);
			    			  if(f1.length() <=0) {
			    					//System.out.println("IT'S EMPTY");
			    					setAlreadySomethingWrittenFac(false);
			    				}else {
			    					//System.out.println("IT'S NOT EMPTY");;
			    					setAlreadySomethingWrittenFac(true); 
			    				}
			    			  
			    			    ArrayList<Factura> tempBill = new ArrayList<>(); 
			    			    if(alreadySomethingWrittenFac==true) {
			    			    	ObjectInputStream preex = new ObjectInputStream(new FileInputStream(FileDirection.billFile));
				    				tempBill = (ArrayList<Factura>) preex.readObject();
				    				preex.close(); //I close reader
			    			    }
			    			    
			    			    for(Factura myBill :tempBill) {
			    			    	int nt = IDCreator.getNumber(myBill.getID());
			    			    	if (nt >= n) {
			    			    		n = nt + 1;
			    			    	}
			    			    }
			    			    
				    	  	}catch(FileNotFoundException e1) {
			    				e1.printStackTrace();
			    			}catch(IOException e2) {
			    				e2.printStackTrace();
			    			}catch(ClassNotFoundException e3) {
			    				e3.printStackTrace();
			    			}
				    	  
				    	  Factura p1 = new Factura(IDCreator.getID("V", n),F2.getText(),total);	 
			    		  
			    		  try {
			    			  //I read the disc to extract pre-existent ArrayList into a temporal ArrayList
			    			  //A little validation first
			    			  File f1 = new File(FileDirection.billFile);
			    			  if(f1.length() <=0) {
			    					//System.out.println("IT'S EMPTY");
			    					setAlreadySomethingWrittenFac(false);
			    				}else {
			    					//System.out.println("IT'S NOT EMPTY");;
			    					setAlreadySomethingWrittenFac(true); 
			    				}
			    			  
			    			    ArrayList<Factura> tempBill = new ArrayList<>(); 
			    			    if(alreadySomethingWrittenFac==true) {
			    			    	ObjectInputStream preex = new ObjectInputStream(new FileInputStream(FileDirection.billFile));
				    				tempBill = (ArrayList<Factura>) preex.readObject();
				    				preex.close(); //I close reader
				    				
			    			    }
			    				//I add the new article to the inventory
			    				tempBill.add(p1);
			    				
			    				//I delete the old File represented by this temporal receptor f1
			    				File f3 = new File(FileDirection.billFile);
			    				f3.delete();
			    				//I create a new file
			    				File f4 = new File(FileDirection.billFile);
			
			    				//I write the updated ArrayList into the new file
			    			    
			    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f4));
			    				oos.writeObject(tempBill);
			    				oos.close(); //I close writer
			    				if(alreadySomethingWrittenFac==false) {
			    					alreadySomethingWrittenFac=true;	
			    				}
			    				
			    			}catch(FileNotFoundException e1) {
			    				e1.printStackTrace();
			    			}catch(IOException e2) {
			    				e2.printStackTrace();
			    			}catch(ClassNotFoundException e3) {
			    				e3.printStackTrace();
			    			}
			    		  total = 0;
				    	  PIF.setText(null);
			    		  
		    		  }else {
		    			  Error2.setText("Saldo insufuciente");
		    		  }
		    		  
	    		  }else{
	    			  Error2.setText("Valores no validos");
	    		  }
		    	  
		      }
		});
		factura.add(Fin2);
		
		//Button for returning to the first page
		JButton BackM = new JButton("Menu");
		BackM.setBounds(10, 250, 90, 20);
		BackM.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  //
		    	  //If the client or cashier regretted something, no problem,
		    	  //Because in previousState I have a backup of the inventory before
		    	  //the sale
		    	  if(!TheSaleWasPayed) {
		    		  try {
			    		  
				    		//Now I need to delete current inventory and replace it with the backup
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
							    
							  //I write the backup ArrayList into the new file
			    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f2));
			    				oos.writeObject(previousState);
			    				oos.close(); //I close writer
				    		  
				    	  }catch (IOException e) {
								e.printStackTrace();
						  }  
		    	  } 
		    	 
		    	  //
		    	  factura.setVisible(false);
		    	  menu.setVisible(true);
		    	  F.setText(null);
		    	  IDI.setText(null);
		    	  CI.setText(null);
		    	  total = 0;
		    	  PF.setText("Valor");
		    	  PIF.setText(null);
		    	  
		    	  PIF.setVisible(true);
		    	  Fin2.setVisible(true);
		      }
		});
		factura.add(BackM);
		
		//Button for returning to previous page
		JButton Back2 = new JButton("Atrás");
		Back2.setBounds(110, 250, 90, 20);
		Back2.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  //
		    	//If the client or cashier regretted something, no problem,
		    	  //Because in previousState I have a backup of the inventory before
		    	  //the sale
		    	  if(!TheSaleWasPayed) {
		    		  try {
			    		  
				    		//Now I need to delete current inventory and replace it with the backup
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
							    
							  //I write the backup ArrayList into the new file
			    				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f2));
			    				oos.writeObject(previousState);
			    				oos.close(); //I close writer
				    		  
				    	  }catch (IOException e) {
								e.printStackTrace();
						  }  
		    	  } 
		    	  
		    	  //
		    	  factura.setVisible(false);
		    	  F.setText(null);
		    	  agregar();
		    	  IDI.setText(null);
		    	  CI.setText(null);
		    	  total = 0;
		    	  PF.setText("Valor");
		    	  PIF.setText(null);
		    	  
		    	  PIF.setVisible(true);
		    	  Fin2.setVisible(true);
		      }
		});
		factura.add(Back2);
		
		factura.add(F2);
		factura.setVisible(false);
	}

	
	//Method for setting features when is opened this page "ventas"
	@SuppressWarnings("unchecked")
	public void agregar() {
		//
		TheSaleWasPayed=false;
		//I open reader to get my object for the backup
		try {
			ObjectInputStream inventOIS = new ObjectInputStream(new FileInputStream(FileDirection.inventoryFile));
			previousState  = (ArrayList<Product>) inventOIS.readObject();
			inventOIS.close(); //I close reader
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		venta.setVisible(true);
		LocalDate today = LocalDate.now();
		String formattedDate = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
		F.append(formattedDate +"\n" + "Nueva Era" +"\n\n" + "Producto\tCantidad\tValor\n");
	}
	
}
