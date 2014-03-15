package Middleware.Util;

import Middleware.Enums.MessageType;

public class DataContainer 
{
	private byte[] byteArray;
	
	public DataContainer()
	{
		this.byteArray = new byte[0];
	}
	
	public DataContainer(byte[] byteArray)
	{
		this.byteArray = trimByteArray(byteArray);
	}
	
	public void addId(int integer)
	{
		mergeByteArrays(intToByteArray(integer));
	}
	
	public void addLength(int integer)
	{
		mergeByteArrays(intToByteArray(integer));
	}
	
	public void addType(MessageType messageType)
	{
		byte[] b = new byte[1];
		b[0] = messageType.getByte();
		mergeByteArrays(b);
	}
	
	public void addPayload(String string)
	{
		byte[] stringByteArray = string.getBytes();
		mergeByteArrays(stringByteArray);
	}
	
	public void addPayload(byte[] payload)
	{
		mergeByteArrays(payload);
	}
	
	public void addPayload(int integer)
	{
		mergeByteArrays(intToByteArray(integer));
	}
	
	public byte[] toByteArray()
	{
		return this.byteArray;
	}
	
	public int getId()
	{
		byte[] idByteArray = new byte[4];
		
		for (int i = 0; i < 4; i++) 
		{
			idByteArray[i] = this.byteArray[i];
		}
		
		return byteArrayToInt(idByteArray);
	}
	
	public byte getType()
	{
		return this.byteArray[4];
	}
	
	public int getLength()
	{
		byte[] lengthByteArray = new byte[4];
		
		for (int i = 5; i < 9; i++) 
		{
			lengthByteArray[i - 5] = this.byteArray[i];
		}
		
		return byteArrayToInt(lengthByteArray);
	}
	
	public int getLengthOfContainer()
	{
		return this.byteArray.length;
	}
	
	public boolean isValid()
	{
		return getLength() == getLengthOfContainer();
	}
	
	public Class<?> getClassType()
	{
		return null;
	}
	
	private void mergeByteArrays(byte[] newByteArray)
	{
		byte[] result = new byte[byteArray.length + newByteArray.length];
		// copy a to result
		System.arraycopy(byteArray, 0, result, 0, byteArray.length);
		// copy b to result
		System.arraycopy(newByteArray, 0, result, byteArray.length, newByteArray.length);
		
		this.byteArray = result;
	}
	
	private byte[] intToByteArray(int integer) 
	{
        return new byte[] {
            (byte)(integer & 0xff),
            (byte)(integer >> 8 & 0xff),
            (byte)(integer >> 16 & 0xff),
            (byte)(integer >>> 24)
        };
	}
	
	private int byteArrayToInt(byte[] temperature) 
	{
	    int number = ((temperature[0] & 0xFF)) | ((temperature[1] & 0xFF) << 8) |
	                ((temperature[2] & 0xFF) << 16) | (temperature[3] & 0xFF << 24);
	 
	    return number;
	}
	
	private byte[] trimByteArray(byte[] byteArray)
	{
		int i = byteArray.length - 1;
		
	    while(byteArray[i] == 0)
	    {
	        --i;
	    }
	    
	    byte[] newByteArray = new byte[i + 1];
	    
		for (int j = 0; j < newByteArray.length; j++) 
		{
			newByteArray[j] = byteArray[j];
		}
		
		return newByteArray;
	}
}
