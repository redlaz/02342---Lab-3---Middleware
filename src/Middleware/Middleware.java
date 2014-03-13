package Middleware;

import java.io.IOException;

import Middleware.Enums.DataType;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;
import Middleware.Interfaces.IMiddleware;
import Middleware.Udp.Udp;

public class Middleware implements IEventRaised
{	
	private Udp udp;
	private IMiddleware caller;
	
	public void publishEvent(Object event) throws IOException
	{	
		if (event instanceof String)
			udp.multicast(DataType.STRING, event, MessageType.EVENT);
		
		if (event instanceof Integer)
			udp.multicast(DataType.INTEGER, event, MessageType.EVENT);
	}
	
	public void subscribe()
	{
		
	}
	
	public void send()
	{
		
	}
	
	public void start(IMiddleware caller) throws IOException
	{
		udp = new Udp(this);
		this.caller = caller;
	}
	
	public void stop()
	{
		
	}

	@Override
	public void eventArrived() 
	{
		caller.handleEvent();
	}
}
