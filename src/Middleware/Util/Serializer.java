package Middleware.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
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
	
	public static Object back(byte[] byteArray) throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
	    ObjectInput in = new ObjectInputStream(bis);
	    return in.readObject();
	}
}
