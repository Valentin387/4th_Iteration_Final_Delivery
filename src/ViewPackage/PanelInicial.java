package ViewPackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ModelPackage.FileDirection;

public class PanelInicial {
	//Attributes
	private JLabel hi;
	public JPanel menu; 
	
	//Constructor
	public PanelInicial(String type) {
		//I instantiate some directions in disc
		FileDirection.inventoryFile="Inventory.dat";
		FileDirection.billFile="Bill.dat";
		
		//Features of the JFrame
		JFrame app = new JFrame("PDV Nueva Era - Menu");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(510, 400);
		app.setLayout(null);
		app.setResizable(false);
		app.setLocationRelativeTo(null);
		
		//I set the first panel that appears when the app is opened
		menu = new JPanel();
		menu.setSize(510,400);
		app.add(menu);
		menu.setLayout(null);
		
		hi = new JLabel("Tipo de cuenta: "+ type);
		hi.setBounds(10, 30, 150, 20);
		
		menu.add(hi);
		
		//I create and instantiate the "JPanel" for adding products 
		Venta venta = new Venta(app, menu);
		Inventory inventory = new Inventory(app,menu,type);
		Bill bill = new Bill(app,menu,type);
		
		File f3 = new File(FileDirection.billFile);
		File f2 = new File(FileDirection.inventoryFile);
		
		if(f3.length() <=0) {
			//System.out.println("IT'S EMPTY");
			venta.setAlreadySomethingWrittenFac(false);
		}else {
			//System.out.println("IT'S NOT EMPTY");;
			venta.setAlreadySomethingWrittenFac(true);
		}
		
		if(f2.length() <=0) {
			//System.out.println("IT'S EMPTY");
			venta.setAlreadySomethingWrittenInv(false);
		}else {
			//System.out.println("IT'S NOT EMPTY");
			venta.setAlreadySomethingWrittenInv(true);
		}
		
		//This button goes back to the login menu
		JButton exit = new JButton("Cerrar sesion");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				app.dispose();
				Login login = new Login();
				login.setVisible(true);
			}
		});
		
		//Another features for the design
		exit.setBounds(10, 10, 150, 20);
		menu.add(exit);
	
		//This button makes invisible (menu) and "opens" (ventas) 
		JButton NewSale = new JButton("Nueva Venta");
		NewSale.addActionListener(new ActionListener() {
			  @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  menu.setVisible(false);
		    	  app.setTitle("PDV Nueva Era - Ventas");
		    	  venta.agregar();
		      }}
		);
		
		//Another features for the design 
		NewSale.setBounds(175, 50, 150, 20);
		menu.add(NewSale);
		
		/////////////////////////////////////////////////////////////////////////////Inventory
		//I create and instantiate the "JPanel" for checking the Inventory
		
		//I create a File object "f1" to be the receptor of
		//a hypothetical File called "Inventory.dat" that may already exists or not
		//in this Java Project's folder. If it already exists it simply becomes the
		//existing file and if it doesn't exist, then the file is created in the folder
		File f1 = new File(FileDirection.inventoryFile);
		if(f1.length() <=0) {
			//System.out.println("IT'S FUCKING EMPTY");
			inventory.setAlreadySomethingWritten(false);
		}else {
			//System.out.println("IT'S NOT EMPTY");
			inventory.setAlreadySomethingWritten(true);
		}
		
		//This button makes invisible (menu) and "opens" (Bill) 
		
		JButton CheckBill = new JButton("Revisar Facturas");
		CheckBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(false);
				app.setTitle("PDV Nueva Era - Facturas");
				bill.agregar();
			}
		});
		
		//Another features for the design
		CheckBill.setBounds(175, 100, 150, 20);
		menu.add(CheckBill);
		
		//This button makes invisible (menu) and "opens" (inventory) 
		JButton CheckInv = new JButton("Revisar Inventario");
		CheckInv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.setVisible(false);
				app.setTitle("PDV Nueva Era - Inventario");
				app.setSize(575,400);
				inventory.agregar();
			}
		});
		
		//Another features for the design
		CheckInv.setBounds(175, 150, 150, 20);
		menu.add(CheckInv);
		
		JButton Delete = new JButton("Borrar cuentas");
		Delete.setBounds(175, 290, 150, 20);
		Delete.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent arg0) {
			    	//I use this receptor f1 to delete the file
				    File f1 = new File(FileDirection.accountFile);
				    f1.delete();
				    //I create a new File under the same name
				    File f2 = new File(FileDirection.accountFile);
				    try {
						f2.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				    
				    app.dispose();
				    Login login = new Login();
					login.setVisible(true);
				}
			});
		if (type.equals("admin")) {
			menu.add(Delete); //I add the button to the JPanel
		}
		
		//////////////////////////////////////////////////////////////////////////////
		app.setVisible(true);
	}
	
	
	//Method for doing Visible of menu = true
	public void mostrar() {
		menu.setVisible(true);
	}
}
