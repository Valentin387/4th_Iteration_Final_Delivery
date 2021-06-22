package ControllerPackage;

public class IDCreator {
	
	public static String getID(String t, int n) {
		String x = "";
		if (n < 10) {
			x = t + "000" + n;
		}
		else if (n < 100) {
			x = t + "00" + n;
		}
		else if (n < 1000) {
			x = t + "0" + n;
		}
		else if (n < 10000) {
			x = t + n;
		}
		return x;
	}
	
	public static int getNumber(String t) {
		int n = Integer.parseInt(t.replaceAll("[\\D]", ""));
		return n;
	}
}
