package kn.hqup.gamexo.ai.utils;

public class LoggerAI {

//	private static boolean printAllowed = true; 
	private static boolean printAllowed = false; 

	public static void p(String message) {
		if (printAllowed)
			System.out.println("logAI* " + message);
	}

}
