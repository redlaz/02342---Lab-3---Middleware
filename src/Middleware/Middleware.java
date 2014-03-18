package Middleware;

import Middleware.Broadcast.BroadcastController;
import Middleware.Enums.MessageType;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Exceptions.MiddlewareNotStartedException;
import Middleware.Interfaces.IMiddleware;
import Middleware.Interfaces.Broadcast.IBroadcastController;
import Middleware.Interfaces.Broadcast.IBroadcastMiddleware;

public class Middleware implements IBroadcastController, IBroadcastMiddleware
{	
	private BroadcastController controller;
	private IMiddleware middlewareCaller;
	private boolean started = false;
	
	public void publish(Object event) throws MiddlewareNotStartedException
	{	
		if (!started)
			throw new MiddlewareNotStartedException("Middleware has to be started before use");
		
		controller.addOutPacket(event, MessageType.EVENT);
	}
	
	public void subscribe(Class<?> classType) throws MiddlewareNotStartedException
	{
		if (!started)
			throw new MiddlewareNotStartedException("Middleware has to be started before use");
		
		controller.addSubscription(classType);
	}
	
	public void start(IMiddleware caller) throws MiddlewareIOException
	{
		started = true;
		controller = new BroadcastController(this);
		this.middlewareCaller = caller;
	}
	
	public void stop()
	{
		controller.stopThreads();
		started = false;
		controller.clear();
		controller = null;
	}
	
	public void activateDebugPrints(boolean flag) throws MiddlewareNotStartedException
	{
		if (controller == null)
			throw new MiddlewareNotStartedException("Middleware not started");
		
		controller.activateDebugPrints(flag);
	}

	@Override
	public void eventArrived(Object object) 
	{
		middlewareCaller.objectReceived(object);
	}
}
