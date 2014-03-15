package Middleware.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Serializer 
{
	public static byte[] now(Object object)
	{
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutput out = null;
		
		try 
		{
			out = new ObjectOutputStream(b);
			out.writeObject(object);
		} 
		
		catch (IOException e) 	
		{
		
		}
		
		return b.toByteArray();
	}
}
