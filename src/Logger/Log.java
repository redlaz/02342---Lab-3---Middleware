package Logger;

public class Log 
{
	public static void Output(String output, Object object)
	{
		System.out.println(object.toString() + ": " + output);
	}
}
