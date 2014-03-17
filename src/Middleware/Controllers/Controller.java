package Middleware.Controllers;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import Middleware.Enums.MessageType;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Interfaces.IMiddlewareCallback;
import Middleware.Interfaces.IInPacketHandlerCallback;
import Middleware.Models.Data;

public class Controller implements IInPacketHandlerCallback
{
	private IMiddlewareCallback middleware;	
	private BlockingQueue<Data> ingoingPacketQueue;
	private BlockingQueue<Data> outgoingPacketQueue;
	private List<Class<?>> subscriptionClasses;

	public Controller(IMiddlewareCallback middleware) throws MiddlewareIOException 
	{
		initialize();
		this.middleware = middleware;
		

		try 
		{
			// Receive and queue packets (thread)
			new PacketReceiver(ingoingPacketQueue).start();
			
			// Handle in-going packets 
			new InPacketHandler(ingoingPacketQueue, subscriptionClasses, this).start();

			// Handle out-going packets 
			new OutPacketHandler(outgoingPacketQueue).start();
		} 
		
		catch (IOException e) 
		{
			throw new MiddlewareIOException(e.getMessage());
		}
	}
	
	private void initialize()
	{
		ingoingPacketQueue = new ArrayBlockingQueue<Data>(1024);
		outgoingPacketQueue = new ArrayBlockingQueue<Data>(1024);
		subscriptionClasses = new ArrayList<Class<?>>();
	}
	
	public void addToSubscriptions(Class<?> classType)
	{
		subscriptionClasses.add(classType);
	}
	
	public void removeFromSubscriptions(Class<?> classType)
	{
		if (subscriptionClasses.contains(classType))
			subscriptionClasses.remove(classType);
	}
	
	public void addOutgoingPacketToQueue(Object event, MessageType messageType) 
	{		
		Data data = new Data(messageType, 5,  event);		
		outgoingPacketQueue.add(data);	
		System.out.println("Out packet queued");
	}

	public void clear() 
	{
		ingoingPacketQueue.clear();
		outgoingPacketQueue.clear();
		subscriptionClasses.clear();
	}
	
	@Override
	public void payloadReceived(Object object) 
	{
		middleware.eventArrived(object);
	}	
}