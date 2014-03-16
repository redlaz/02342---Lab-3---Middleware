package Middleware;

import java.io.IOException;
import Middleware.Controllers.Controller;
import Middleware.Enums.MessageType;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddlewareCallback;
import Middleware.Interfaces.IMiddlewareEvent;

public class Middleware implements IMiddlewareCallback
{	
	private Controller udp;
	private IMiddlewareEvent caller;
	private boolean started = false;
	
	public void publish(Object event) throws MiddlewareNotStartedException
	{	
		if (!started)
			throw new MiddlewareNotStartedException("Middleware has to be started before use");
		
		udp.addOutgoingPacketToQueue(event, MessageType.EVENT);
	}
	
	public void subscribe(Class<?> cls) throws MiddlewareNotStartedException
	{
		if (!started)
			throw new MiddlewareNotStartedException("Middleware has to be started before use");
		
		udp.addToSubscriptions(cls);
	}
	
	public void start(IMiddlewareEvent caller) throws MiddlewareIOException
	{
		started = true;
		udp = new Controller(this);
		this.caller = caller;
	}
	
	public void stop()
	{
		started = false;
		udp.clear();
		udp = null;
	}

	@Override
	public void eventArrived(Object object) 
	{
		caller.objectReceivedFromMiddleware(object);
	}
}
