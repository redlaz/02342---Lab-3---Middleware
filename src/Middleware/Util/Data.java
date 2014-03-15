package Middleware.Util;

import java.io.Serializable;
import Middleware.Enums.MessageType;


public class Data implements Serializable
{
	private MessageType messageType;
	private int id;
	private int length;
	private Object payload;
	
	public Data(MessageType messageType, int id, Object payload) 
	{
		this.messageType = messageType;
		this.id = id;
		this.payload = payload;
	}
	
	public Data(byte[] packet)
	{
		
	}

	public MessageType getMessageType() 
	{
		return messageType;
	}
	
	public void setMessageType(MessageType messageType) 
	{
		this.messageType = messageType;
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public int getLength() 
	{
		return length;
	}
	
	public void setLength(int length) 
	{
		this.length = length;
	}
	
	public Object getPayload() 
	{
		return payload;
	}
	
	public void setPayload(Object payload) 
	{
		this.payload = payload;
	}
}
