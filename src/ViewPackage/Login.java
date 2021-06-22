package ViewPackage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ModelPackage.Account;
import ModelPackage.Factura;
import ModelPackage.FileDirection;

import ControllerPackage.IDCreator;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

  private JPanel contentPane;
  private JTextField tf1;
  private JTextField tf2;
  private JLabel labelResultado;
  
  private String permiso;
  
  public boolean alreadySomethingWrittenAcc;


  public Login() {
	FileDirection.accountFile="Account.dat";
	
	File f = new File(FileDirection.accountFile);
	
	if(f.length() <= 0) {
		//System.out.println("IT'S EMPTY");
		alreadySomethingWrittenAcc = false;
	}else {
		//System.out.println("IT'S NOT EMPTY");;
		alreadySomethingWrittenAcc = true;
	}
	  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 220);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);
    setResizable(false);
	setLocationRelativeTo(null);
	setTitle("PDV Nueva Era - Login");
    
    JLabel t1 = new JLabel("Usuario:");
    t1.setBounds(23, 38, 193, 14);
    contentPane.add(t1);
    
    tf1 = new JTextField();
    tf1.setBounds(200, 35, 193, 20);
    contentPane.add(tf1);
    tf1.setColumns(10);
    
    JLabel lblPrecio = new JLabel("Contraseña:");
    lblPrecio.setBounds(23, 74, 95, 14);
    contentPane.add(lblPrecio);
    
    tf2 = new JTextField();
    tf2.setBounds(200, 71, 193, 20);
    contentPane.add(tf2);
    tf2.setColumns(10);
    
    labelResultado = new JLabel("");
    labelResultado.setBounds(200, 152, 229, 14);
 
    
    JButton btnAlta = new JButton("Ingresar");
    btnAlta.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        labelResultado.setText("");        
        try {
        	File f1 = new File(FileDirection.accountFile);
			ArrayList<Account> tempAcc = new ArrayList<>(); 
			if(alreadySomethingWrittenAcc) {
				ObjectInputStream preex = new ObjectInputStream(new FileInputStream(FileDirection.accountFile));
				tempAcc = (ArrayList<Account>) preex.readObject();
				preex.close(); //I close reader
			}
			
			boolean login = false;
			String type = "";
			
			for(Account myAccount :tempAcc) {
				if (myAccount.getUser().equals(tf1.getText()) && myAccount.getPassword().equals(tf2.getText())){
					login = true;
					type = myAccount.getType();
				}
			}
			
			if (login) {
				PanelInicial test = new PanelInicial(type);
				dispose();
			}else {
				labelResultado.setText("Informacion incorrecta");
			}
			
			    
  		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(IOException e2) {
			e2.printStackTrace();
		}catch(ClassNotFoundException e3) {
			e3.printStackTrace();
		}
      }
    });
    btnAlta.setBounds(200, 118, 89, 23);
    contentPane.add(btnAlta);
   
    contentPane.add(labelResultado);
    
    JButton btnCrear = new JButton("Crear");
    btnCrear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        labelResultado.setText("");        
        try {
			File f1 = new File(FileDirection.accountFile);

			ArrayList<Account> tempAcc = new ArrayList<>(); 
			if(alreadySomethingWrittenAcc) {
				ObjectInputStream preex = new ObjectInputStream(new FileInputStream(FileDirection.accountFile));
				tempAcc = (ArrayList<Account>) preex.readObject();
				preex.close(); //I close reader
				
			}
			
			boolean success = true;
			
			for(Account myAccount :tempAcc) {
				if (tf1.getText().isEmpty() || tf1.getText().isEmpty()) {
					success = false;
				}
				if (myAccount.getUser().equals(tf1.getText())){
					success = false;
				}
			}
			
			if (success) {
				String ty = "user";
				if (tf1.getText().equals("admin")) {
					ty = "admin";
				}
				Account a = new Account(tf1.getText(),tf2.getText(),ty);
				
				tempAcc.add(a);
				
				//I delete the old File represented by this temporal receptor f1
				File f3 = new File(FileDirection.accountFile);
				f3.delete();
				//I create a new file
				File f4 = new File(FileDirection.accountFile);

				//I write the updated ArrayList into the new file
			    
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f4));
				oos.writeObject(tempAcc);
				oos.close(); //I close writer
				
				tf1.setText("");
		        tf2.setText("");
		        labelResultado.setText("Usuario creado");
		        alreadySomethingWrittenAcc = true;
			}else {
				labelResultado.setText("Usuario invalido");
			}
			
			
			    
		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		}catch(IOException e2) {
			e2.printStackTrace();
		}catch(ClassNotFoundException e3) {
			e3.printStackTrace();
		}  
      }
    });
    btnCrear.setBounds(300, 118, 89, 23);
    contentPane.add(btnCrear);
    
    
    
    }

}