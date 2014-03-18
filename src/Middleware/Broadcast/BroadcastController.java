package Middleware.Broadcast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import Logger.Log;
import Middleware.Enums.MessageType;
import Middleware.Exceptions.MiddlewareIOException;
import Middleware.Interfaces.Broadcast.IBroadcastController;
import Middleware.Interfaces.Broadcast.IBroadcastInPacketHandler;
import Middleware.Models.Data;

public class BroadcastController implements IBroadcastInPacketHandler
{
	private IBroadcastController middleware;	
	private BlockingQueue<Data> inQueue;
	private BlockingQueue<Data> outQueue;
	private List<Class<?>> subscriptions;
	private Receiver receiver;
	private InHandler inHandler;
	private OutHandler outHandler;
		

	public BroadcastController(IBroadcastController middleware) throws MiddlewareIOException 
	{
		initialize();
		this.middleware = middleware;
		
		try 
		{
			receiver = new Receiver(inQueue);
			receiver.start();
			
			inHandler = new InHandler(inQueue, this);
			inHandler.start();

			outHandler = new OutHandler(outQueue);
			outHandler.start();
		} 
		
		catch (IOException e) 
		{
			throw new MiddlewareIOException(e.getMessage());
		}
	}
	
	private void initialize()
	{
		inQueue = new ArrayBlockingQueue<Data>(10000);
		outQueue = new ArrayBlockingQueue<Data>(10000);
		subscriptions = new ArrayList<Class<?>>();
	}
	
	public void stopThreads()
	{
		receiver.stopThread();
		inHandler.stopThread();
		outHandler.stopThread();
	}
	
	public void activateDebugPrints(boolean flag)
	{
		Log.printToConsole(flag);
	}
	
	public void addSubscription(Class<?> classType)
	{
		subscriptions.add(classType);
	}
	
	public void removeSubscription(Class<?> classType)
	{
		if (subscriptions.contains(classType))
			subscriptions.remove(classType);
	}
	
	public void addOutPacket(Object event, MessageType messageType) 
	{		
		Data data = new Data(messageType, 5,  event);		
		outQueue.add(data);	
		Log.Output("Out packet queued", this);
	}

	public void clear() 
	{
		inQueue.clear();
		outQueue.clear();
		subscriptions.clear();
	}
	
	@Override
	public void payloadReceived(Data data) 
	{
		if (subscriptions.contains(data.getPayload().getClass()))
			middleware.eventArrived(data.getPayload());
	}	
}