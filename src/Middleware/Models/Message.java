package Middleware.Models;

import java.io.Serializable;

import Middleware.Enums.MessageType;




public class Message implements Serializable
{
	private MessageType messageType;
	private int id;
	private int length;
	private Object payload;

	public MessageType getType() 
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
		return this.payload;
	}
	
	public void setPayload(Object payload) 
	{
		this.payload = payload;
	}
}
