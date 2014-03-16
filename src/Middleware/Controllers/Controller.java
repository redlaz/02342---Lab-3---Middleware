package Middleware.Controllers;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Logger.Log;
import Middleware.Enums.MessageType;
import Middleware.Interfaces.IEventRaised;
import Middleware.Interfaces.IIngoingPacketHandled;
import Middleware.Models.Data;
import Middleware.Util.Serializer;

public class Controller implements IIngoingPacketHandled
{
	
	private IEventRaised middleware;
	
	private BlockingQueue<Data> ingoingPacketQueue;
	private BlockingQueue<Data> outgoingPacketQueue;

	private List<Class<?>> subscriptionClasses;

	public Controller(IEventRaised middleware) throws IOException 
	{
		ingoingPacketQueue = new ArrayBlockingQueue<Data>(1024);
		outgoingPacketQueue = new ArrayBlockingQueue<Data>(1024);
		subscriptionClasses = new ArrayList<Class<?>>();
		this.middleware = middleware;
		
		// Receive and queue packets (thread)
		new DatagramPacketReceiver(ingoingPacketQueue).start();

		// Handle in-going packets 
		new IngoingPacketQueueHandler(ingoingPacketQueue, subscriptionClasses, this).start();

		// Handle out-going packets 
		new OutPacketHandler(outgoingPacketQueue).start();
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
	
	public void addOutgoingPacketToQueue(Object event, MessageType messageType) throws IOException
	{		
		Data data = new Data(messageType, 5,  event);		
		outgoingPacketQueue.add(data);	
		System.out.println("Out packet queued");
	}

	@Override
	public void payloadReceived(Object object) 
	{
		middleware.eventArrived(object);
	}	
}