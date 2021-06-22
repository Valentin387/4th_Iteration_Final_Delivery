package ControllerPackage;

public class Check {
	
	//Public static methods for validating information in specific if statement in "ventas"
	
	public static boolean integer(String n) {
		try{
	        Integer.parseInt(n);
	    }catch(Exception e ){
	        return false;
	    }
	    return true;
	}
	
	public static boolean decimal(String n) {
		try{
	        Double.parseDouble(n);
	    }catch(Exception e ){
	        return false;
	    }
	    return true;
	}
}
