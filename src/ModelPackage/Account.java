package ModelPackage;

import java.io.Serializable;

public class Account implements Serializable{

	private String User;
	private String Password;
	private String Type;
	
	
	//Constructor
	public Account(String user, String password, String type) {
		super();
		User = user;
		Password = password;
		Type = type;
	}
	
	//getters and setters
	public String getUser() {
		return User;
	}

	public void setUser(String iD) {
		User = iD;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getType() {
		return Type;
	}
	public void setPrice(String type) {
		Type = type;
	}
}
