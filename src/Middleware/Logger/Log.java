package Middleware.Logger;

public class Log 
{
	private static boolean printingToConsoleActivated = false;
	
	public static void Output(String output, Object object)
	{
		if (printingToConsoleActivated)
			System.out.println(object.toString() + ": " + output);
	}
	
	public static void printToConsole(boolean flag)
	{
		printingToConsoleActivated = flag;
	}
}
