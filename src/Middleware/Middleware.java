package Middleware;

import java.io.IOException;
import Middleware.Controllers.Controller;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;
import Middleware.Interfaces.IMiddleware;

public class Middleware implements IEventRaised
{	
	private Controller udp;
	private IMiddleware caller;
	
	public void publishEvent(Object event) throws IOException
	{	
		udp.addOutgoingPacketToQueue(event, MessageType.EVENT);
	}
	
	public void subscribe(Class<?> cls)
	{
		udp.addToSubscriptions(cls);
	}
	
	public void send()
	{
		
	}
	
	public void start(IMiddleware caller) throws IOException
	{
		udp = new Controller(this);
		this.caller = caller;
	}
	
	public void stop()
	{
		
	}

	@Override
	public void eventArrived(Object object) 
	{
		caller.handleEvent(object);
	}
}
